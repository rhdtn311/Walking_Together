package backend.server.exception.userService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class PhoneNumberDuplicationException extends BusinessException {
    public PhoneNumberDuplicationException() {
        super(ErrorCode.PHONE_NUMBER_DUPLICATION);
    }
}
