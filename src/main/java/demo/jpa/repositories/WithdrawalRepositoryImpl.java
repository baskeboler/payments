package demo.jpa.repositories;

import demo.jpa.entities.Account;
import demo.jpa.entities.TransactionState;
import demo.jpa.entities.WithdrawalTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by victor on 8/17/15.
 */
public class WithdrawalRepositoryImpl extends CustomRepoImplementation<WithdrawalTransaction>
        implements WithdrawalRepositoryCustom {
    private static final Logger LOG = LoggerFactory.getLogger(WithdrawalRepositoryImpl.class);

    @Transactional
    @Override
    public WithdrawalTransaction processWithdrawal(WithdrawalTransaction txn) {
        LOG.info("Processing withdrawal.");
        Account a = txn.getAccount();
        BigDecimal accountBalance = a.getAccountBalance();
        BigDecimal amount = txn.getAmount();
        if (amount.compareTo(accountBalance) <= 0) {
            LOG.info("Withdrawal is valid. Modifying account balance.");
            accountBalance = accountBalance.subtract(amount);
            a.setAccountBalance(accountBalance);
            getEm().merge(a);
            txn.setProcessed(true);
            txn.setState(TransactionState.OK);
            save(txn);
        } else {
            LOG.error("Amount ({}) is greater than account balance ({}). Ignoring transaction.", amount, accountBalance);
            txn.setProcessed(true);
            txn.setState(TransactionState.FAILED);
            save(txn);
        }
        return txn;
    }
}
