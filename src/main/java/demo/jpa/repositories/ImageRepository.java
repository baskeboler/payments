package demo.jpa.repositories;

import demo.jpa.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by victor on 8/21/15.
 */
@Transactional(readOnly = true)
public interface ImageRepository extends JpaRepository<Image, Long> {
}
