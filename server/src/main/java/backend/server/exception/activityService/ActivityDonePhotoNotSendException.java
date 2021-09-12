package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityDonePhotoNotSendException extends BusinessException {
    public ActivityDonePhotoNotSendException() {
        super(ErrorCode.ACTIVITY_DONE_PHOTO_NOT_SEND);
    }
}
