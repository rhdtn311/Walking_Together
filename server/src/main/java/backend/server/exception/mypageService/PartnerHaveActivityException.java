package backend.server.exception.mypageService;

import backend.server.exception.BusinessException;
import backend.server.exception.ErrorCode;

public class PartnerHaveActivityException extends BusinessException {
    public PartnerHaveActivityException() {
        super(ErrorCode.PARTNER_HAVE_ACTIVITY);
    }
}
