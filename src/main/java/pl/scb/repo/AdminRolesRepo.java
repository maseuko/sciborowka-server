package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.AdminRoles;

import java.util.List;
import java.util.Optional;

public interface AdminRolesRepo extends JpaRepository<AdminRoles, Long> {
    Optional<List<AdminRoles>> findByAdminId(long id);
}
