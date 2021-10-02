package backend.server.exception.userService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class EmailDuplicationException extends BusinessException {
    public EmailDuplicationException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
