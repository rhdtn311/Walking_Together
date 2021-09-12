package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityAbnormalDoneWithoutMinimumDistanceException extends BusinessException {
    public ActivityAbnormalDoneWithoutMinimumDistanceException() {
        super(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE);
    }
}
