package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.Images;

import java.util.List;

public interface ImagesRepo extends JpaRepository<Images,Long> {
    List<Images> findByPostId(long id);
    List<Images> findByCategoryId(long id);
}
