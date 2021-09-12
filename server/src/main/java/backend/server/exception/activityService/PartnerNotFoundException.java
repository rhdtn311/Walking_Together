package backend.server.exception.activityService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class PartnerNotFoundException extends BusinessException {

    public PartnerNotFoundException() {
        super(ErrorCode.PARTNER_NOT_FOUND);
    }

}
