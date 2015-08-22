package demo.services.impl;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.entities.Transaction;
import demo.jpa.entities.WithdrawalTransaction;
import demo.jpa.repositories.DepositRepository;
import demo.jpa.repositories.TransactionRepository;
import demo.jpa.repositories.WithdrawalRepository;
import demo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by victor on 8/12/15.
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository txns;
    @Autowired
    DepositRepository deposits;
    @Autowired
    WithdrawalRepository withdrawals;

    @Override
    public List<Transaction> findByAccount(Account a) {
        return txns.findByAccount(a);
    }

    @Override
    public List<Transaction> findByAccount(Account a, Pageable pageable) {
        return txns.findByAccount(a, pageable);
    }

    @Override
    public Transaction saveTransaction(Transaction txn) {
        return txns.save(txn);
    }

    @Override
    public DepositTransaction saveDeposit(DepositTransaction deposit) {
        return deposits.save(deposit);
    }

    @Override
    public WithdrawalTransaction saveWithdrawal(WithdrawalTransaction withdrawal) {
        return withdrawals.save(withdrawal);
    }

    @Override
    public void delete(Transaction txn) {
        txns.delete(txn);
    }

    public TransactionRepository getTxns() {
        return txns;
    }

    public void setTxns(TransactionRepository txns) {
        this.txns = txns;
    }

    public DepositRepository getDeposits() {
        return deposits;
    }

    public void setDeposits(DepositRepository deposits) {
        this.deposits = deposits;
    }

    public WithdrawalRepository getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(WithdrawalRepository withdrawals) {
        this.withdrawals = withdrawals;
    }
}
