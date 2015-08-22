package demo.jpa.entities;

import demo.BankingApplication;
import demo.jpa.entities.builders.AccountBuilder;
import demo.jpa.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by victor on 8/5/15.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BankingApplication.class)
public class AccountTest {

    //private Logger LOG
    @Autowired
    private AccountRepository repo;

    @Before
    public void setup() {
        this.repo.deleteAll();
        this.repo.save(AccountBuilder.anAccount().withName("Victor Gil").withAddress("8 de Octubre 2323").build());
    }

    @Test
    public void testFind() {
        List<Account> a = this.getRepo().findByName("Victor Gil");
        assertTrue("La cuenta recien creada debe existir", a.size() == 1);
    }

    public AccountRepository getRepo() {
        return this.repo;
    }

    public void setRepo(AccountRepository repo) {
        this.repo = repo;
    }
}