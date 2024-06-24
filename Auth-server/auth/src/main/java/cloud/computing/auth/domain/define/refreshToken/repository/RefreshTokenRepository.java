package cloud.computing.auth.domain.define.refreshToken.repository;

import cloud.computing.auth.domain.define.refreshToken.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

