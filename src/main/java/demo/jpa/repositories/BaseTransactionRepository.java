package demo.jpa.repositories;

import demo.jpa.entities.Account;
import demo.jpa.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by victor on 8/5/15.
 */
@NoRepositoryBean
public interface BaseTransactionRepository<T extends Transaction> extends JpaRepository<T, Long> {
    T findOne(Long id);

    List<T> findAll();

    List<T> findAllBy(Pageable pageable);

    List<T> findByAccount(Account account);

    List<T> findByAccount(Account account, Pageable pageable);

    List<T> findByProcessed(boolean processed);

    Page<T> findByProcessed(boolean processed, Pageable pageable);

    List<T> findByAccountId(long id);
}
