package demo.jpa.entities;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Created by victor on 8/6/15.
 */
@Entity
public class DepositTransaction extends Transaction {
    private BigDecimal amount;
    private String comment;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
