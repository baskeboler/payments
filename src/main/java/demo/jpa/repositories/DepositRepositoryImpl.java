package demo.jpa.repositories;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.entities.TransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by victor on 8/12/15.
 */
public class DepositRepositoryImpl extends CustomRepoImplementation<DepositTransaction>
        implements DepositRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(DepositRepositoryImpl.class);

    @Transactional
    @Override
    public DepositTransaction processDeposit(DepositTransaction d) {
        DepositRepositoryImpl.LOG.info("processing deposit");
        Account account = d.getAccount();
        BigDecimal accountBalance = account.getAccountBalance();
        account.setAccountBalance(accountBalance.add(d.getAmount()));
        d.setProcessed(true);
        d.setState(TransactionState.OK);
        this.getEm().merge(account);
        return save(d);
    }

}
