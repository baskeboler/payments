package demo.jpa.repositories;

import demo.jpa.entities.Bank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by victor on 8/18/15.
 */
@Transactional(readOnly = true)
public interface BankRepository extends JpaRepository<Bank, Long>{
    List<Bank> findByName(String name);
    List<Bank> findAllBy(Pageable pageable);
}
