package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import demo.jpa.entities.Account;
import demo.jpa.entities.City;
import demo.jpa.entities.Country;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.repositories.BankRepository;
import demo.jpa.repositories.CityRepository;
import demo.jpa.repositories.CountryRepository;
import demo.jpa.repositories.DepositRepository;
import demo.reactor.TransactionReactorConfig;
import demo.services.AccountService;
import demo.web.forms.AccountForm;
import demo.web.forms.DepositForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static reactor.bus.Event.wrap;

/**
 * Created by victor on 8/9/15.
 */
@SpringView(name = AccountsView.VIEW_NAME)
public class AccountsView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "accounts";
    private static final int PAGE_SIZE = 25;
    private final MTable<Account> list = new MTable<>(Account.class)
            .withProperties("id", "bank", "name", "address", "accountBalance")
            .withGeneratedColumn("cityname", entity -> entity.getCity().getName() + ", " + entity.getCity().getCountry().getName())
            .withColumnHeaders("Id", "Bank", "Name", "Address", "Balance", "City")
            .withFullWidth();
    @Autowired
    private TransactionReactorConfig transactionReactorConfig;
    private Account selectedAccount;
    private final Button viewAccount = new MButton(FontAwesome.ARROW_RIGHT, this::goToAccountPage);
    @Autowired
    private AccountService accountService;
    private final Button refresh = new MButton(FontAwesome.REFRESH, clickEvent -> this.listAccounts());
    @Autowired
    private DepositRepository deposits;
    private final Button deposit = new MButton(FontAwesome.MONEY, this::deposit);
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;
    private final Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private final Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private final Button delete = new ConfirmButton(
            FontAwesome.TRASH_O,
            "Are you sure you want to delete this account?",
            this::remove);

    public TransactionReactorConfig getTransactionReactorConfig() {
        return transactionReactorConfig;
    }

    public void setTransactionReactorConfig(TransactionReactorConfig transactionReactorConfig) {
        this.transactionReactorConfig = transactionReactorConfig;
    }

    public DepositRepository getDeposits() {
        return deposits;
    }

    public void setDeposits(DepositRepository deposits) {
        this.deposits = deposits;
    }

    private void add(Button.ClickEvent clickEvent) {
        edit(new Account());
    }

    private void edit(Account account) {
        List<Country> countries = this.countryRepository.findAll();
        List<City> cities = this.cityRepository.findAll();
        AccountForm form = new AccountForm(account, new LazyList<>(
                firstRow -> this.bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / LazyList.DEFAULT_PAGE_SIZE,
                                LazyList.DEFAULT_PAGE_SIZE
                        )
                ),
                () -> (int) this.bankRepository.count(),
                LazyList.DEFAULT_PAGE_SIZE
        ), countries, cities);
        form.openInModalPopup();
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);
    }

    private void goToAccountPage(Button.ClickEvent clickEvent) {
        if (this.selectedAccount != null) {
            this.getUI().getSession().setAttribute(Account.class, this.selectedAccount);
            this.getUI().getNavigator().navigateTo(AccountView.VIEW_NAME);
        }
    }

    private void resetEntry(Account account) {
        listAccounts();
        closeWindow();
    }

    @SuppressWarnings("unchecked")
    private void listAccounts() {
        list.setBeans(
                new SortableLazyList<>(
                        (firstRow, sortAscending, property) -> accountService.getPage(
                                new PageRequest(
                                        firstRow / AccountsView.AccountsView.PAGE_SIZE,
                                        AccountsView.AccountsView.PAGE_SIZE,
                                        sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC,
                                        property == null ? "id" : property
                                )
                        ),
                        accountService::count,
                        AccountsView.AccountsView.PAGE_SIZE
                )
        );
        adjustActionButtonState();
    }

    private void closeWindow() {
        getUI().getWindows().stream()
                .forEach(w -> getUI().removeWindow(w));
    }

    private void adjustActionButtonState() {
        boolean hasSelection = list.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
        deposit.setEnabled(hasSelection);
        viewAccount.setEnabled(hasSelection);
    }

    private void saveEntry(Account account) {
        accountService.saveAccount(account);
        listAccounts();
        closeWindow();
    }

    private void edit(Button.ClickEvent e) {
        edit(list.getValue());
    }

    private void remove(Button.ClickEvent e) {
        accountService.deleteAccount(list.getValue().getId());
        list.setValue(null);
        listAccounts();
    }

    private void deposit(Button.ClickEvent e) {
        DepositTransaction depo = new DepositTransaction();
        depo.setAccount(list.getValue());
        DepositForm form = new DepositForm(depo);
        form.openInModalPopup();
        form.setSavedHandler(this::saveDeposit);
        form.setResetHandler(this::resetDeposit);
    }

    private void resetDeposit(DepositTransaction depositTransaction) {
        Notification.show("Deposit not saved.", Notification.Type.TRAY_NOTIFICATION);
        closeWindow();
    }

    private void saveDeposit(DepositTransaction depositTransaction) {
        depositTransaction.setTimestamp(Timestamp.from(Instant.now()));
        depositTransaction = deposits.save(depositTransaction);
        this.transactionReactorConfig
                .getEventBus()
                .notify("txn.deposit", wrap(depositTransaction));
        closeWindow();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.listAccounts();
        Notification.show("Deposit saved.", Notification.Type.TRAY_NOTIFICATION);
    }

    @PostConstruct
    public void init() {
        addComponents(
                new RichText().withMarkDownResource("/static/accounts.md"),
                new MHorizontalLayout(this.refresh, addNew, edit, delete, deposit, viewAccount),
                list
        );
        listAccounts();
        list.addMValueChangeListener(mValueChangeEvent -> adjustActionButtonState());
        list.addMValueChangeListener(event -> this.selectedAccount = event.getValue());
        list.setSelectable(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public BankRepository getBankRepository() {
        return this.bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public CountryRepository getCountryRepository() {
        return this.countryRepository;
    }

    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CityRepository getCityRepository() {
        return this.cityRepository;
    }

    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
}
