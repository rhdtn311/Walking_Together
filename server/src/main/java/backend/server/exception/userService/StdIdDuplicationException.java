package backend.server.exception.userService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class StdIdDuplicationException extends BusinessException {
    public StdIdDuplicationException() {
        super(ErrorCode.STDID_DUPLICATION);
    }
}
