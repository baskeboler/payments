package demo.jpa.repositories;

import demo.jpa.entities.City;
import demo.jpa.entities.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by victor on 8/19/15.
 */
@Transactional(readOnly = true)
public interface CityRepository extends JpaRepository<City, Long>{
    List<City> findByCountry(Country country);
    List<City> findByCountry(Country country, Pageable pageable);
}
