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

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private boolean processed = false;

    public boolean isProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (this.getId() != that.getId()) return false;
        return this.getAccount().equals(that.getAccount());

    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ this.getId() >>> 32);
        result = 31 * result + this.getAccount().hashCode();
        return result;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }
}
