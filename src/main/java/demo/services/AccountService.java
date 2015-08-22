package demo.services;

import demo.jpa.entities.Account;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by victor on 8/6/15.
 */
public interface AccountService {
    Account createAccount(String name, String Address);
    Account saveAccount(Account account);
    Account find(long id);

    List<Account> findByName(String name);
    List<Account> getPage(Pageable pageable);
    void deleteAccount(long id);
    void deleteAccount(Account a);
    int count();
}
