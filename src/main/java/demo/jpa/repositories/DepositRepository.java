package demo.jpa.repositories;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by victor on 8/6/15.
 */
@Transactional(readOnly = true)
public interface DepositRepository extends BaseTransactionRepository<DepositTransaction>, DepositRepositoryCustom{
        List<DepositTransaction> findByAccount(Account a);

}
