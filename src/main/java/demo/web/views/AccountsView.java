package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import demo.jpa.entities.*;
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
import org.springframework.data.domain.Sort.Direction;
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

    @Autowired
    private TransactionReactorConfig transactionReactorConfig;
    private Account selectedAccount = null;

    public TransactionReactorConfig getTransactionReactorConfig() {
        return this.transactionReactorConfig;
    }

    public void setTransactionReactorConfig(TransactionReactorConfig transactionReactorConfig) {
        this.transactionReactorConfig = transactionReactorConfig;
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositRepository deposits;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    public DepositRepository getDeposits() {
        return this.deposits;
    }

    public void setDeposits(DepositRepository deposits) {
        this.deposits = deposits;
    }

    private final MTable<Account> list = new MTable<>(Account.class)
            .withProperties("id", "bank", "name", "address", "accountBalance")
            .withGeneratedColumn("cityname", entity -> entity.getCity().getName() + ", " + entity.getCity().getCountry().getName())
            .withColumnHeaders("Id", "Bank", "Name", "Address", "Balance", "City")
            .withFullWidth();

    private final Button refresh = new MButton(FontAwesome.REFRESH, clickEvent -> listAccounts());
    private final Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private final Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private final Button delete = new ConfirmButton(
            FontAwesome.TRASH_O,
            "Are you sure you want to delete this account?",
            this::remove);
    private final Button deposit = new MButton(FontAwesome.MONEY, this::deposit);
    private final Button viewAccount = new MButton(FontAwesome.ARROW_RIGHT, this::goToAccountPage);

    private void add(ClickEvent clickEvent) {
        this.edit(new Account());
    }

    private void goToAccountPage(ClickEvent clickEvent) {
        if (selectedAccount != null) {
            getUI().getSession().setAttribute(Account.class, selectedAccount);
            getUI().getNavigator().navigateTo(AccountView.VIEW_NAME);
        }
    }

    private void edit(Account account) {
        List<Country> countries = countryRepository.findAll();
        List<City> cities = cityRepository.findAll();
        AccountForm form = new AccountForm(account, new LazyList<>(
                firstRow -> bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / LazyList.DEFAULT_PAGE_SIZE,
                                LazyList.DEFAULT_PAGE_SIZE
                        )
                ),
                () -> (int) bankRepository.count(),
                LazyList.DEFAULT_PAGE_SIZE
        ), countries, cities);
        form.openInModalPopup();
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);
    }

    private void resetEntry(Account account) {
        this.listAccounts();
        this.closeWindow();
    }

    private void saveEntry(Account account) {
        this.accountService.saveAccount(account);
        this.listAccounts();
        this.closeWindow();
    }

    private void closeWindow() {
        this.getUI().getWindows().stream()
                .forEach(w -> this.getUI().removeWindow(w));
    }

    private void listAccounts() {
        this.list.setBeans(
                new SortableLazyList<>(
                        (firstRow, sortAscending, property) -> this.accountService.getPage(
                                new PageRequest(
                                        firstRow / AccountsView.PAGE_SIZE,
                                        AccountsView.PAGE_SIZE,
                                        sortAscending ? Direction.ASC : Direction.DESC,
                                        property == null ? "id" : property
                                )
                        ),
                        () -> this.accountService.count(),
                        AccountsView.PAGE_SIZE
                )
        );
        this.adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = this.list.getValue() != null;
        this.edit.setEnabled(hasSelection);
        this.delete.setEnabled(hasSelection);
        this.deposit.setEnabled(hasSelection);
        this.viewAccount.setEnabled(hasSelection);
    }

    private void edit(ClickEvent e) {
        this.edit(this.list.getValue());
    }

    private void remove(ClickEvent e) {
        this.accountService.deleteAccount(this.list.getValue().getId());
        this.list.setValue(null);
        this.listAccounts();
    }

    private void deposit(ClickEvent e) {
        DepositTransaction depo = new DepositTransaction();
        depo.setAccount(this.list.getValue());
        DepositForm form = new DepositForm(depo);
        form.openInModalPopup();
        form.setSavedHandler(this::saveDeposit);
        form.setResetHandler(this::resetDeposit);
    }

    private void resetDeposit(DepositTransaction depositTransaction) {
        Notification.show("Deposit not saved.", Type.TRAY_NOTIFICATION);
        this.closeWindow();
    }

    private void saveDeposit(DepositTransaction depositTransaction) {
        depositTransaction.setTimestamp(Timestamp.from(Instant.now()));
        depositTransaction = this.deposits.save(depositTransaction);
        transactionReactorConfig
                .getEventBus()
                .notify("txn.deposit", wrap(depositTransaction));
        this.closeWindow();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listAccounts();
        Notification.show("Deposit saved.", Type.TRAY_NOTIFICATION);
    }

    @PostConstruct
    public void init() {
        this.addComponents(
                new RichText().withMarkDownResource("/static/accounts.md"),
                new MHorizontalLayout(refresh, this.addNew, this.edit, this.delete, this.deposit, this.viewAccount),
                this.list
        );
        this.listAccounts();
        this.list.addMValueChangeListener(mValueChangeEvent -> this.adjustActionButtonState());
        this.list.addMValueChangeListener(event -> selectedAccount = event.getValue());
        this.list.setSelectable(true);
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }

    public AccountService getAccountService() {
        return this.accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public BankRepository getBankRepository() {
        return bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
}
