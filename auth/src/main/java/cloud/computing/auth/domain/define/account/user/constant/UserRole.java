package cloud.computing.auth.domain.define.account.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("관리자"),
    UNAUTH("미인증"),
    USER("유저");

    private final String text;
}