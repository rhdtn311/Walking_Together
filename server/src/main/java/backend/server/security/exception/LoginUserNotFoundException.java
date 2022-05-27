package backend.server.security.exception;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class LoginUserNotFoundException extends BusinessException {
    public LoginUserNotFoundException() {
        super(ErrorCode.LOGIN_USER_NOT_FOUND_EXCEPTION);
    }
}
