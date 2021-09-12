package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class MinimumActivityDistanceNotSatisfyException extends BusinessException {
    public MinimumActivityDistanceNotSatisfyException() {
        super(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY);
    }

}
