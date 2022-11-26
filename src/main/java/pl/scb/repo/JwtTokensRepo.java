package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.scb.models.JwtTokens;

import java.util.List;
import java.util.Optional;

public interface JwtTokensRepo extends JpaRepository<JwtTokens, Long> {
    List<Optional<JwtTokens>> findByUserId(long id);
    @Query(value = "SELECT * FROM sciborowka.jwt_tokens WHERE auth_token=?1 AND refresh_token=?2",nativeQuery = true)
    Optional<JwtTokens> findByAuthTokenAndRefreshToken(String auth, String refresh);

    @Query(value = "SELECT * FROM sciborowka.jwt_tokens WHERE auth_token=?1 AND refresh_token=?2 AND user_id=?3",nativeQuery = true)
    Optional<JwtTokens> findByAuthTokenAndRefreshTokenAndUserId(String auth, String refresh, long id);
}
