package demo.services.impl;

import demo.jpa.entities.Account;
import demo.jpa.entities.builders.AccountBuilder;
import demo.jpa.repositories.AccountRepository;
import demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by victor on 8/6/15.
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository repo;

    public AccountRepository getRepo() {
        return this.repo;
    }

    public void setRepo(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Account createAccount(String name, String address) {
        return this.repo.save(
                AccountBuilder.anAccount()
                        .withName(name)
                        .withAddress(address)

                        .build()
        );
    }

    @Override
    public Account saveAccount(Account account) {
        return this.repo.save(account);
    }

    @Override
    public Account find(long id) {
        return this.repo.findOne(id);
    }

    @Override
    public List<Account> findByName(String name) {
        return this.repo.findByNameLike(name);
    }

    @Override
    public List<Account> getPage(Pageable pageable) {
        return this.repo.findAllBy(pageable);
    }

    @Override
    public void deleteAccount(long id) {
        this.repo.delete(id);
    }

    @Override
    public void deleteAccount(Account a) {
        this.repo.delete(a);
    }

    @Override
    public int count() {
        return (int) this.repo.count();
    }
}
