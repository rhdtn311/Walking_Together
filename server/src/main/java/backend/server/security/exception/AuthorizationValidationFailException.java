package backend.server.security.exception;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class AuthorizationValidationFailException extends BusinessException {
    public AuthorizationValidationFailException() {
        super(ErrorCode.AUTHORIZATION_FAIL_EXCEPTION);
    }
}
