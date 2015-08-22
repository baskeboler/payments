package demo.jpa.repositories;

import demo.jpa.entities.WithdrawalTransaction;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by victor on 8/6/15.
 */
@Transactional(readOnly = true)
public interface WithdrawalRepository extends BaseTransactionRepository<WithdrawalTransaction>,
        WithdrawalRepositoryCustom {

}
