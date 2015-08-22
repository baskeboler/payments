package demo.jpa.repositories;

import demo.jpa.entities.WithdrawalTransaction;

/**
 * Created by victor on 8/17/15.
 */
public interface WithdrawalRepositoryCustom {
    WithdrawalTransaction processWithdrawal(WithdrawalTransaction txn);
}
