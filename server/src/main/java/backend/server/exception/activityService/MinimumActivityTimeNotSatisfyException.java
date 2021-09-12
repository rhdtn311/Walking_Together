package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class MinimumActivityTimeNotSatisfyException extends BusinessException {
    public MinimumActivityTimeNotSatisfyException() {
        super(ErrorCode.MINIMUM_ACTIVITY_TIME_NOT_SATISFY);
    }

}
