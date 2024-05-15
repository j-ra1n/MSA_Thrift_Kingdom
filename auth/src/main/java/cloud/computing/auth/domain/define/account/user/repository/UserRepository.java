package cloud.computing.auth.domain.define.account.user.repository;

import cloud.computing.auth.domain.define.account.user.User;
import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPlatformIdAndPlatformType(String platformId, UserPlatformType platformType);

}
