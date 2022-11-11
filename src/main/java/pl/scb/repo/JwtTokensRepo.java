package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.JwtTokens;

import java.util.List;
import java.util.Optional;

public interface JwtTokensRepo extends JpaRepository<JwtTokens, Long> {
    List<Optional<JwtTokens>> findByUserId(long id);
}
