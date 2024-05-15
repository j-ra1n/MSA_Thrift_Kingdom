package cloud.computing.auth.api.controller.response;

import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthLoginPageResponse {
    private UserPlatformType platformType;
    private String url;

    @Builder
    public AuthLoginPageResponse(UserPlatformType platformType, String url) {
        this.platformType = platformType;
        this.url = url;
    }
}