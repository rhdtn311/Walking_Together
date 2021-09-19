package backend.server.exception.feedService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ReviewNotReceiveException  extends BusinessException {
    public ReviewNotReceiveException() {
        super(ErrorCode.REVIEW_NOT_RECEIVE);
    }
}
