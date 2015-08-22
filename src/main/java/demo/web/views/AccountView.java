package demo.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
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
import org.springframework.data.domain.Sort.Direction;
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

    public static final String VIEW_NAME = "account";

    private static final int PAGE_SIZE = 50;
    private Account account;
    private AccountForm accountForm;

    private Label balance = new Label();

    private MTable<DepositTransaction> depositsList = new MTable<>(DepositTransaction.class)
            .withCaption("Deposits")
            .withProperties("id", "amount", "timestamp", "state")
            .withColumnHeaders("Id", "Amount", "Date", "State");

    private MTable<WithdrawalTransaction> withdrawalsList = new MTable<>(WithdrawalTransaction.class)
            .withCaption("Withdrawals")
            .withProperties("id", "amount", "timestamp", "state")
            .withColumnHeaders("Id", "Amount", "Date", "State");

    public AccountRepository getAccountRepository() {
        return this.accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public WithdrawalRepository getWithdrawalRepository() {
        return this.withdrawalRepository;
    }

    public void setWithdrawalRepository(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    private Button withdrawal = new MButton("Create withdrawal", clickEvent -> newWithdrawalDialog());

    private Button deposit = new MButton("Create deposit", event -> newDepositDialog());

    private Button update = new MButton(FontAwesome.REFRESH, event -> update());

    private void newDepositDialog() {
        DepositTransaction t = new DepositTransaction();
        t.setAccount(account);
        DepositForm form = new DepositForm(t);
        form.openInModalPopup();
        form.setSavedHandler(this::saveDeposit);
        form.setResetHandler(this::resetDeposit);
    }

    private void resetDeposit(DepositTransaction depositTransaction) {
        closeWindow();
    }

    private void saveDeposit(DepositTransaction depositTransaction) {
        depositTransaction.setTimestamp(Timestamp.from(Instant.now()));
        depositTransaction = depositRepository.save(depositTransaction);
        transactionReactorConfig.getEventBus()
                .notify("txn.deposit", wrap(depositTransaction));
        closeWindow();
        update();
    }

    private void newWithdrawalDialog() {
        WithdrawalTransaction t = new WithdrawalTransaction();
        t.setAccount(account);
        WithdrawalForm form = new WithdrawalForm(t);
        form.openInModalPopup();
        form.setSavedHandler(this::saveWithdrawal);
        form.setResetHandler(this::resetWithdrawal);
    }

    private void resetWithdrawal(WithdrawalTransaction withdrawalTransaction) {
        closeWindow();
    }

    private void saveWithdrawal(WithdrawalTransaction withdrawalTransaction) {
        withdrawalTransaction.setTimestamp(Timestamp.from(Instant.now()));
        withdrawalTransaction = withdrawalRepository.save(withdrawalTransaction);

        transactionReactorConfig
                .getEventBus()
                .notify("txn.withdrawal", wrap(withdrawalTransaction));

        closeWindow();
        update();
    }

    private void closeWindow() {
        getUI().getWindows().stream().forEach(window -> getUI().removeWindow(window));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //   viewChangeEvent.
        Account a = getUI().getSession().getAttribute(Account.class);
        if (a != null) {
            account = a;
            accountForm.setEntity(a);
            accountForm.setReadOnly(true);
            listDeposits();
            listWithdrawals();
        }
        //depositsList.
        //depositsList.getRowGenerator().generateRow())
    }

    private void listDeposits() {
        depositsList.setBeans(new SortableLazyList<DepositTransaction>(
                (firstRow, sortAscending, property) -> depositRepository.findByAccount(account, new PageRequest(
                                firstRow / PAGE_SIZE,
                                PAGE_SIZE,
                                sortAscending ? Direction.ASC : Direction.DESC,
                                (property == null) ? "id" : property
                        )
                ),
                () -> depositRepository.findByAccount(account).size(),
                PAGE_SIZE
        ));
    }

    private void listWithdrawals() {
        withdrawalsList.setBeans(new SortableLazyList<WithdrawalTransaction>(
                (firstRow, sortAscending, property) -> withdrawalRepository.findByAccount(account, new PageRequest(
                        firstRow / PAGE_SIZE,
                        PAGE_SIZE,
                        sortAscending ? Direction.ASC : Direction.DESC,
                        (property == null) ? "id" : property
                )),
                () -> withdrawalRepository.findByAccount(account).size(),
                PAGE_SIZE
        ));
    }

    private void update() {
        account = accountRepository.findOne(account.getId());
        accountForm.setEntity(account);
        listDeposits();
        listWithdrawals();
    }

    @PostConstruct
    public void init() {
        accountForm = new AccountForm(null, new LazyList<>(
                firstRow -> bankRepository.findAllBy(
                        new PageRequest(
                                firstRow / LazyList.DEFAULT_PAGE_SIZE,
                                LazyList.DEFAULT_PAGE_SIZE
                        )
                ),
                () -> (int) bankRepository.count(),
                LazyList.DEFAULT_PAGE_SIZE
        ), countryRepository.findAll(), cityRepository.findAll());
        accountForm.setReadOnly(true);
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(depositsList.withFullWidth().withFullHeight(), "Deposits");
        tabSheet.addTab(withdrawalsList.withFullWidth().withFullHeight(), "Withdrawals");
        //tabSheet.se
        addComponents(
                new RichText().withMarkDownResource("/static/account.md"),
                new MHorizontalLayout(update, withdrawal, deposit),

                new MHorizontalLayout().withFullWidth()
                        .with(accountForm)
                        .expand(
                                tabSheet
                        )

        );
    }

    public TransactionReactorConfig getTransactionReactorConfig() {
        return transactionReactorConfig;
    }

    public void setTransactionReactorConfig(TransactionReactorConfig transactionReactorConfig) {
        this.transactionReactorConfig = transactionReactorConfig;
    }

    public BankRepository getBankRepository() {
        return this.bankRepository;
    }

    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public DepositRepository getDepositRepository() {
        return this.depositRepository;
    }

    public void setDepositRepository(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
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
