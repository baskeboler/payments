package demo.jpa.repositories;

import demo.jpa.entities.DepositTransaction;

/**
 * Created by victor on 8/12/15.
 */
public interface DepositRepositoryCustom {
    DepositTransaction processDeposit(DepositTransaction d);
}
