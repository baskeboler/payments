package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.entities.WithdrawalTransaction;
import demo.jpa.repositories.*;
import demo.reactor.TransactionReactorConfig;
import demo.web.forms.AccountForm;
import demo.web.forms.DepositForm;
import demo.web.forms.WithdrawalForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;

import static reactor.bus.Event.wrap;

/**
 * Created by victor on 8/18/15.
 */
@SpringView(name = AccountView.VIEW_NAME)
public class AccountView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "account";
    private static final int PAGE_SIZE = 50;
    private final Label balance = new Label();
    private final MTable<DepositTransaction> depositsList = new MTable<>(DepositTransaction.class)
            .withCaption("Deposits")
            .withProperties("id", "amount", "timestamp", "state")
            .withColumnHeaders("Id", "Amount", "Date", "State");
    private final MTable<WithdrawalTransaction> withdrawalsList = new MTable<>(WithdrawalTransaction.class)
            .withCaption("Withdrawals")
            .withProperties("id", "amount", "timestamp", "state")
            .withColumnHeaders("Id", "Amount", "Date", "State");
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private TransactionReactorConfig transactionReactorConfig;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;
    private Account account;
    private AccountForm accountForm;
    private final Button withdrawal = new MButton("Create withdrawal", clickEvent -> this.newWithdrawalDialog());
    private final Button deposit = new MButton("Create deposit", event -> this.newDepositDialog());
    private final Button update = new MButton(FontAwesome.REFRESH, event -> this.update());

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public WithdrawalRepository getWithdrawalRepository() {
        return withdrawalRepository;
    }

    public void setWithdrawalRepository(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    private void newDepositDialog() {
        DepositTransaction t = new DepositTransaction();
        t.setAccount(this.account);
        DepositForm form = new DepositForm(t);
        form.openInModalPopup();
        form.setSavedHandler(this::saveDeposit);
        form.setResetHandler(this::resetDeposit);
    }

    private void resetDeposit(DepositTransaction depositTransaction) {
        this.closeWindow();
    }

    private void closeWindow() {
        this.getUI().getWindows().stream().forEach(window -> this.getUI().removeWindow(window));
    }

    private void saveDeposit(DepositTransaction depositTransaction) {
        depositTransaction.setTimestamp(Timestamp.from(Instant.now()));
        depositTransaction = this.depositRepository.save(depositTransaction);
        this.transactionReactorConfig.getEventBus()
                .notify("txn.deposit", wrap(depositTransaction));
        this.closeWindow();
        this.update();
    }

    private void update() {
        this.account = this.accountRepository.findOne(this.account.getId());
        this.accountForm.setEntity(this.account);
        this.listDeposits();
        this.listWithdrawals();
    }

    @SuppressWarnings("unchecked")
    private void listDeposits() {
        this.depositsList.setBeans(new SortableLazyList<>(
                (firstRow, sortAscending, property) -> this.depositRepository.findByAccount(this.account, new PageRequest(
                                firstRow / AccountView.PAGE_SIZE,
                                AccountView.PAGE_SIZE,
                                sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC,
                                property == null ? "id" : property
                        )
                ),
                () -> this.depositRepository.findByAccount(this.account).size(),
                AccountView.PAGE_SIZE
        ));
    }

    @SuppressWarnings("unchecked")
    private void listWithdrawals() {
        this.withdrawalsList.setBeans(new SortableLazyList<>(
                (firstRow, sortAscending, property) -> this.withdrawalRepository.findByAccount(this.account, new PageRequest(
                        firstRow / AccountView.PAGE_SIZE,
                        AccountView.PAGE_SIZE,
                        sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC,
                        property == null ? "id" : property
                )),
                () -> this.withdrawalRepository.findByAccount(this.account).size(),
                AccountView.PAGE_SIZE
        ));
    }

    private void newWithdrawalDialog() {
        WithdrawalTransaction t = new WithdrawalTransaction();
        t.setAccount(this.account);
        WithdrawalForm form = new WithdrawalForm(t);
        form.openInModalPopup();
        form.setSavedHandler(this::saveWithdrawal);
        form.setResetHandler(this::resetWithdrawal);
    }

    private void resetWithdrawal(WithdrawalTransaction withdrawalTransaction) {
        this.closeWindow();
    }

    private void saveWithdrawal(WithdrawalTransaction withdrawalTransaction) {
        withdrawalTransaction.setTimestamp(Timestamp.from(Instant.now()));
        withdrawalTransaction = this.withdrawalRepository.save(withdrawalTransaction);

        this.transactionReactorConfig
                .getEventBus()
                .notify("txn.withdrawal", wrap(withdrawalTransaction));

        this.closeWindow();
        this.update();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
        //   viewChangeEvent.
        Account a = this.getUI().getSession().getAttribute(Account.class);
        if (a != null) {
            this.account = a;
            this.accountForm.setEntity(a);
            this.accountForm.setReadOnly(true);
            this.listDeposits();
            this.listWithdrawals();
        }
        //depositsList.
        //depositsList.getRowGenerator().generateRow())
    }

    @PostConstruct
    public void init() {
        this.accountForm = new AccountForm(null, new LazyList<>(
                firstRow -> this.bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / LazyList.DEFAULT_PAGE_SIZE,
                                LazyList.DEFAULT_PAGE_SIZE
                        )
                ),
                () -> (int) this.bankRepository.count(),
                LazyList.DEFAULT_PAGE_SIZE
        ), this.countryRepository.findAll(), this.cityRepository.findAll());
        this.accountForm.setReadOnly(true);
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(this.depositsList.withFullWidth().withFullHeight(), "Deposits");
        tabSheet.addTab(this.withdrawalsList.withFullWidth().withFullHeight(), "Withdrawals");
        //tabSheet.se
        this.addComponents(
                new RichText().withMarkDownResource("/static/account.md"),
                new MHorizontalLayout(this.update, this.withdrawal, this.deposit),

                new MHorizontalLayout().withFullWidth()
                        .with(this.accountForm)
                        .expand(
                                tabSheet
                        )

        );
    }

    public TransactionReactorConfig getTransactionReactorConfig() {
        return this.transactionReactorConfig;
    }

    public void setTransactionReactorConfig(TransactionReactorConfig transactionReactorConfig) {
        this.transactionReactorConfig = transactionReactorConfig;
    }

    public BankRepository getBankRepository() {
        return bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public DepositRepository getDepositRepository() {
        return depositRepository;
    }

    public void setDepositRepository(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
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
