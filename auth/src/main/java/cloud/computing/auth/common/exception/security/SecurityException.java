package cloud.computing.auth.common.exception.security;

import cloud.computing.auth.common.exception.AuthException;
import cloud.computing.auth.common.exception.ExceptionMessage;

public class SecurityException extends AuthException {
    public SecurityException(ExceptionMessage message) {
        super(message.getText());
    }
}
