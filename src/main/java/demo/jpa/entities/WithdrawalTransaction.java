package demo.jpa.entities;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Created by victor on 8/6/15.
 */
@Entity
public class WithdrawalTransaction extends Transaction {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
