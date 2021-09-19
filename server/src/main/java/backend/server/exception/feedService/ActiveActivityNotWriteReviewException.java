package backend.server.exception.feedService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActiveActivityNotWriteReviewException extends BusinessException {
    public ActiveActivityNotWriteReviewException() {
        super(ErrorCode.ACTIVE_ACTIVITY_NOT_WRITE_REVIEW);
    }
}
