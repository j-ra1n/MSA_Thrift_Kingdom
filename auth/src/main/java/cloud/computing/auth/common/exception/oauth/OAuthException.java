package cloud.computing.auth.common.exception.oauth;

import cloud.computing.auth.common.exception.AuthException;
import cloud.computing.auth.common.exception.ExceptionMessage;

public class OAuthException extends AuthException {

    public OAuthException(ExceptionMessage message) {
        super(message.getText());
    }
}
