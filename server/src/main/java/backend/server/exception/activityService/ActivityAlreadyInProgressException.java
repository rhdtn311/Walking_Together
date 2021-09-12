package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ActivityAlreadyInProgressException extends BusinessException {

   public ActivityAlreadyInProgressException() {
       super(ErrorCode.ACTIVITY_ALREADY_IN_PROGRESS);
   }
}
