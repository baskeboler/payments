package demo.jpa.entities;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * Created by victor on 8/5/15.
 */

@Entity
public class Account {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String address;

    @ManyToOne(optional = false)
    private City city;

    private BigDecimal accountBalance = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    private Bank bank;

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ this.getId() >>> 32);
        result = 31 * result + (this.getName() != null ? this.getName().hashCode() : 0);
        result = 31 * result + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (this.getId() != account.getId()) return false;
        if (this.getName() != null ? !this.getName().equals(account.getName()) : account.getName() != null)
            return false;
        return !(this.getAddress() != null ? !this.getAddress().equals(account.getAddress()) : account.getAddress() != null);

    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).build();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
