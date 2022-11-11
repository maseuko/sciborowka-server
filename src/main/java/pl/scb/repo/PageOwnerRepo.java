package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.PageOwner;

import java.util.Optional;

public interface PageOwnerRepo extends JpaRepository<PageOwner, Long> {
    Optional<PageOwner> findByName(String name);
}
