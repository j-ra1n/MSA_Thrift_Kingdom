package cloud.computing.auth.common.exception.jwt;

import cloud.computing.auth.common.exception.AuthException;
import cloud.computing.auth.common.exception.ExceptionMessage;

public class JwtException extends AuthException {
    public JwtException(ExceptionMessage message) {
        super(message.getText());
    }
}
