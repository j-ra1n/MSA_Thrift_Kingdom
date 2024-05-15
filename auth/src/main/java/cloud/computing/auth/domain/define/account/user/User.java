package cloud.computing.auth.domain.define.account.user;

import cloud.computing.auth.domain.define.account.user.constant.UserRole;
import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "PLATFORM_ID")
    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PLATFORM_TYPE")
    @ColumnDefault(value = "'KAKAO'")
    private UserPlatformType platformType;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    @ColumnDefault(value = "'UNAUTH'")
    private UserRole role;

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;

    @Builder
    public User(String platformId, UserPlatformType platformType, String name, UserRole role, String profileImageUrl) {
        this.platformId = platformId;
        this.platformType = platformType;
        this.name = name;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return platformId + "_" + platformType.name();
    }

    @Override
    public String getPassword() {
        return platformId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
