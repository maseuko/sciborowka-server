package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.scb.models.ImageCategory;

import java.util.List;
import java.util.Optional;

public interface ImageCategoryRepo extends JpaRepository<ImageCategory, Long> {
    Optional<ImageCategory> findByName(String name);
    Optional<List<ImageCategory>> findByOwnerId(long id);
    @Query(value = "SELECT * FROM sciborowka.image_category WHERE name=?1 AND owner_id=?2", nativeQuery = true)
    Optional<ImageCategory> findByNameAndOwnerId(String name, long id);
}
