package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class ActivityMapPhotoNotSendException extends BusinessException {
    public ActivityMapPhotoNotSendException() {
        super(ErrorCode.ACTIVITY_MAP_PHOTO_NOT_SEND);
    }
}
