package demo.jpa.entities.builders;

import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by victor on 8/6/15.
 */
public class DepositTransactionBuilder {
    private BigDecimal amount;
    private String comment;
    private long id;
    private Account account;
    private boolean processed;
    private Timestamp timestamp;

    private DepositTransactionBuilder() {
    }

    public static DepositTransactionBuilder aDepositTransaction() {
        return new DepositTransactionBuilder();
    }

    public DepositTransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public DepositTransactionBuilder withTimestamp(Timestamp instant) {
        this.timestamp = instant;
        return this;
    }

    public DepositTransactionBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public DepositTransactionBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public DepositTransactionBuilder withAccount(Account account) {
        this.account = account;
        return this;
    }

    public DepositTransactionBuilder withProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    public DepositTransactionBuilder but() {
        return aDepositTransaction()
                .withTimestamp(timestamp)
                .withAmount(amount)
                .withComment(comment)
                .withId(id)
                .withAccount(account)
                .withProcessed(processed);
    }

    public DepositTransaction build() {
        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(amount);
        depositTransaction.setComment(comment);
        depositTransaction.setId(id);
        depositTransaction.setAccount(account);
        depositTransaction.setProcessed(processed);
        depositTransaction.setTimestamp(timestamp);
        return depositTransaction;
    }
}
