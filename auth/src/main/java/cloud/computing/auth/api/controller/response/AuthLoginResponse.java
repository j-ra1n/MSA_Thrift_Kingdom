package cloud.computing.auth.api.controller.response;

import cloud.computing.auth.domain.define.account.user.constant.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthLoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserRole role;
    private String nickname;

    @Builder
    public AuthLoginResponse(String accessToken, String refreshToken, UserRole role, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.nickname = nickname;
    }
}
