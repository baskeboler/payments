package demo.services.batch;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

/**
 * Created by victor on 8/12/15.
 */
public class DepositProcessor implements ItemProcessor<DepositTransaction, DepositTransaction> {
    @Override
    public DepositTransaction process(DepositTransaction depositTransaction) throws Exception {
        //BigDecimal amount = depositTransaction.getAmount();
        //final Account account = depositTransaction.getAccount();
        //account.setAccountBalance(account.getAccountBalance().add(amount));
        depositTransaction.setProcessed(true);
        return depositTransaction;
    }
}
