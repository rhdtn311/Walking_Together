package backend.server.exception.noticeService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class NoticeNotFoundException extends BusinessException {
    public NoticeNotFoundException() {
        super(ErrorCode.NOTICE_NOT_FOUND);
    }
}
