package backend.server.exception.noticeService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class DataNotFoundInPageException extends BusinessException {
    public DataNotFoundInPageException() {
        super(ErrorCode.DATA_NOT_FOUND_IN_PAGE);
    }
}