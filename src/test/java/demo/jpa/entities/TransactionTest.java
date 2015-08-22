package demo.jpa.entities;

import demo.BankingApplication;
import demo.jpa.entities.builders.AccountBuilder;
import demo.jpa.entities.builders.DepositTransactionBuilder;
import demo.jpa.entities.builders.WithdrawalTransactionBuilder;
import demo.jpa.repositories.AccountRepository;
import demo.jpa.repositories.DepositRepository;
import demo.jpa.repositories.TransactionRepository;
import demo.jpa.repositories.WithdrawalRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by victor on 8/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BankingApplication.class)
public class TransactionTest {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTest.class);
    @Autowired
    private AccountRepository accounts;

    @Autowired
    private DepositRepository deposits;

    @Autowired
    private WithdrawalRepository withdrawals;
    @Autowired
    private TransactionRepository txns;

    @Before
    public void setUp() throws Exception {
        Account a1 = this.getAccounts().save(
                AccountBuilder.anAccount()
                        .withName("Victor Gil")
                        .withAddress("8 de Octubre 2323")
                        .build()
        );
        Account a2 = this.getAccounts().save(
                AccountBuilder.anAccount()
                        .withName("Ramon Rodriguez")
                        .withAddress("18 de Julio 666")
                        .build()
        );

        this.getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(1000))
                        .withAccount(a1)
                        .withComment("My Deposit")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        this.getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(1500))
                        .withAccount(a1)
                        .withComment("My Deposit2")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        this.getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(750))
                        .withAccount(a2)
                        .withComment("My Deposit3")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        this.getWithdrawals().save(
                WithdrawalTransactionBuilder.aWithdrawalTransaction()
                        .withAccount(a1).withAmount(BigDecimal.valueOf(200))
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
    }

    public AccountRepository getAccounts() {
        return this.accounts;
    }

    public DepositRepository getDeposits() {
        return this.deposits;
    }

    public WithdrawalRepository getWithdrawals() {
        return this.withdrawals;
    }

    public void setWithdrawals(WithdrawalRepository withdrawals) {
        this.withdrawals = withdrawals;
    }

    public void setDeposits(DepositRepository deposits) {
        this.deposits = deposits;
    }

    public void setAccounts(AccountRepository accounts) {
        this.accounts = accounts;
    }

    @After
    public void tearDown() throws Exception {
        this.getDeposits().deleteAll();
        this.getWithdrawals().deleteAll();
        this.getAccounts().deleteAll();
        //getTxns().deleteAll();
        //getAccountService().deleteAll();
    }

    @Test
    public void testFindTransactions() {
        List<Transaction> ts = this.getTxns().findAll();
        TransactionTest.LOG.info("Printing txns.");
        ts.forEach(t -> TransactionTest.LOG.info(t.toString()));
        assertFalse(ts.isEmpty());
    }

    public TransactionRepository getTxns() {
        return this.txns;
    }

    public void setTxns(TransactionRepository txns) {
        this.txns = txns;
    }

    @Test
    public void testFindWithdrawals() {
        List<WithdrawalTransaction> list = this.getWithdrawals().findAll();
        assertTrue("size hould be 1.", list.size() == 1);
    }
}