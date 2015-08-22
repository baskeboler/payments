package demo.jpa.entities.builders;

import demo.jpa.entities.Account;
import demo.jpa.entities.WithdrawalTransaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by victor on 8/6/15.
 */
public class WithdrawalTransactionBuilder {
    private long id;
    private Account account;
    private Timestamp timestamp;
    private boolean processed;
    private BigDecimal amount;

    private WithdrawalTransactionBuilder() {
    }

    public WithdrawalTransactionBuilder but() {
        return WithdrawalTransactionBuilder.aWithdrawalTransaction().withAmount(this.amount).withId(this.id).withAccount(this.account).withTimestamp(this.timestamp).withProcessed(this.processed);
    }

    public WithdrawalTransactionBuilder withProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    public WithdrawalTransactionBuilder withTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WithdrawalTransactionBuilder withAccount(Account account) {
        this.account = account;
        return this;
    }

    public WithdrawalTransactionBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public WithdrawalTransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public static WithdrawalTransactionBuilder aWithdrawalTransaction() {
        return new WithdrawalTransactionBuilder();
    }

    public WithdrawalTransaction build() {
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
        withdrawalTransaction.setId(this.id);
        withdrawalTransaction.setAccount(this.account);
        withdrawalTransaction.setTimestamp(this.timestamp);
        withdrawalTransaction.setProcessed(this.processed);
        withdrawalTransaction.setAmount(this.amount);
        return withdrawalTransaction;
    }
}
