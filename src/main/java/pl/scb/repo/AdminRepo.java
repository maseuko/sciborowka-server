package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.AdminModel;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<AdminModel, Long> {
    Optional<AdminModel> findByLogin(String login);
}