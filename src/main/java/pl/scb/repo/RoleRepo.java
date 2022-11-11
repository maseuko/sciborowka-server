package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
