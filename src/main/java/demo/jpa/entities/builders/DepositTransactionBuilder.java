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

    public DepositTransactionBuilder but() {
        return DepositTransactionBuilder.aDepositTransaction()
                .withTimestamp(this.timestamp)
                .withAmount(this.amount)
                .withComment(this.comment)
                .withId(this.id)
                .withAccount(this.account)
                .withProcessed(this.processed);
    }

    public DepositTransactionBuilder withProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    public DepositTransactionBuilder withAccount(Account account) {
        this.account = account;
        return this;
    }

    public DepositTransactionBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public DepositTransactionBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public DepositTransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public DepositTransactionBuilder withTimestamp(Timestamp instant) {
        timestamp = instant;
        return this;
    }

    public static DepositTransactionBuilder aDepositTransaction() {
        return new DepositTransactionBuilder();
    }

    public DepositTransaction build() {
        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(this.amount);
        depositTransaction.setComment(this.comment);
        depositTransaction.setId(this.id);
        depositTransaction.setAccount(this.account);
        depositTransaction.setProcessed(this.processed);
        depositTransaction.setTimestamp(this.timestamp);
        return depositTransaction;
    }
}
