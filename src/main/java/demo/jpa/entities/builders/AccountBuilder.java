package demo.jpa.entities.builders;

import demo.jpa.entities.Account;

import java.math.BigDecimal;

/**
 * Created by victor on 8/6/15.
 */
public class AccountBuilder {
    private long id;
    private String name;
    private String address;
    private BigDecimal accountBalance=BigDecimal.ZERO ;

    private AccountBuilder() {
    }

    public static AccountBuilder anAccount() {
        return new AccountBuilder();
    }

    public AccountBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public AccountBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AccountBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public AccountBuilder withAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public AccountBuilder but() {
        return anAccount().withId(id).withName(name).withAddress(address).withAccountBalance(accountBalance);
    }

    public Account build() {
        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setAddress(address);
        account.setAccountBalance(accountBalance);
        return account;
    }
}
