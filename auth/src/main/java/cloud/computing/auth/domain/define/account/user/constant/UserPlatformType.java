package cloud.computing.auth.domain.define.account.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPlatformType {

    KAKAO("카카오 로그인"),
    GOOGLE("구글 로그인");

    private final String text;
}