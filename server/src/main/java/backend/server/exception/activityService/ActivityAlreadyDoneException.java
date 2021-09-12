package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityAlreadyDoneException extends BusinessException {
    public ActivityAlreadyDoneException() {
        super(ErrorCode.ACTIVITY_ALREADY_DONE);
    }
}
