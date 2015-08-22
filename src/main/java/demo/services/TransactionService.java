package demo.services;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.entities.Transaction;
import demo.jpa.entities.WithdrawalTransaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by victor on 8/12/15.
 */
public interface TransactionService {
    List<Transaction> findByAccount(Account a);
    List<Transaction> findByAccount(Account a, Pageable pageable);

    Transaction saveTransaction(Transaction txn);
    DepositTransaction saveDeposit(DepositTransaction deposit);
    WithdrawalTransaction saveWithdrawal(WithdrawalTransaction withdrawal);

    void delete(Transaction txn);
}
