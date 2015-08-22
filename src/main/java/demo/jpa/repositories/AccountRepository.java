package demo.jpa.repositories;

import demo.jpa.entities.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by victor on 8/5/15.
 */
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByName(String name);

    List<Account> findByNameLike(String name);

    List<Account> findAllBy(Pageable pageable);
}
