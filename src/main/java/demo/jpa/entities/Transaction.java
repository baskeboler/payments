package demo.jpa.entities;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by victor on 8/5/15.
 */
@Entity
@Inheritance
public abstract class Transaction {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account account;

    private Timestamp timestamp;

    @Enumerated
    private TransactionState state = TransactionState.PENDING;
    private boolean processed;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ getId() >>> 32);
        result = 31 * result + getAccount().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (getId() != that.getId()) return false;
        return getAccount().equals(that.getAccount());

    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).build();
    }

    public long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionState getState() {
        return this.state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }
}
