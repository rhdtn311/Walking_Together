package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityNotFoundException extends BusinessException {

    public ActivityNotFoundException() {
        super(ErrorCode.ACTIVITY_NOT_FOUND);
    }
}
