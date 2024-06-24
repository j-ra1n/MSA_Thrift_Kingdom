package cloud.computing.auth.common.exception.state;

import cloud.computing.auth.common.exception.AuthException;
import cloud.computing.auth.common.exception.ExceptionMessage;

public class LoginStateException extends AuthException {
    public LoginStateException(ExceptionMessage message) {
        super(message.getText());
    }
}

