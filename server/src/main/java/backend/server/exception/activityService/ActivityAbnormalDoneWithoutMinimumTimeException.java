package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityAbnormalDoneWithoutMinimumTimeException extends BusinessException {
    public ActivityAbnormalDoneWithoutMinimumTimeException() {
        super(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME);
    }
}
