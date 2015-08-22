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

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by victor on 8/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BankingApplication.class)
public class TransactionTest {

    private static Logger LOG = LoggerFactory.getLogger(TransactionTest.class);
    @Autowired
    private AccountRepository accounts;

    @Autowired
    private DepositRepository deposits;

    @Autowired
    private WithdrawalRepository withdrawals;

    public WithdrawalRepository getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(WithdrawalRepository withdrawals) {
        this.withdrawals = withdrawals;
    }

    @Autowired
    private TransactionRepository txns;

    public DepositRepository getDeposits() {
        return deposits;
    }

    public void setDeposits(DepositRepository deposits) {
        this.deposits = deposits;
    }

    public AccountRepository getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public void setTxns(TransactionRepository txns) {
        this.txns = txns;
    }

    public TransactionRepository getTxns() {
        return txns;
    }

    @Before
    public void setUp() throws Exception {
        Account a1 = getAccounts().save(
                AccountBuilder.anAccount()
                        .withName("Victor Gil")
                        .withAddress("8 de Octubre 2323")
                        .build()
        );
        Account a2 = getAccounts().save(
                AccountBuilder.anAccount()
                        .withName("Ramon Rodriguez")
                        .withAddress("18 de Julio 666")
                        .build()
        );

        getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(1000))
                        .withAccount(a1)
                        .withComment("My Deposit")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(1500))
                        .withAccount(a1)
                        .withComment("My Deposit2")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        getDeposits().save(
                DepositTransactionBuilder.aDepositTransaction()
                        .withAmount(BigDecimal.valueOf(750))
                        .withAccount(a2)
                        .withComment("My Deposit3")
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
        getWithdrawals().save(
                WithdrawalTransactionBuilder.aWithdrawalTransaction()
                        .withAccount(a1).withAmount(BigDecimal.valueOf(200))
                        .withTimestamp(Timestamp.from(Instant.now())).build()
        );
    }

    @After
    public void tearDown() throws Exception {
        getDeposits().deleteAll();
        getWithdrawals().deleteAll();
        getAccounts().deleteAll();
        //getTxns().deleteAll();
        //getAccountService().deleteAll();
    }

    @Test
    public void testFindTransactions() {
        List<Transaction> ts = getTxns().findAll();
        LOG.info("Printing txns.");
        ts.forEach(t -> LOG.info(t.toString()));
        assertFalse(ts.isEmpty());
    }

    @Test
    public void testFindWithdrawals() {
        List<WithdrawalTransaction> list = getWithdrawals().findAll();
        assertTrue("size hould be 1.", list.size() == 1);
    }
}