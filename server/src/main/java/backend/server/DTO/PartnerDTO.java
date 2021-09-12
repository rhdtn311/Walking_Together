package backend.server.DTO;

import backend.server.entity.Partner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PartnerDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PartnerListRes {

        private Long partnerId;
        private String partnerName;
        private String partnerDetail;
        private String partnerBirth;
        private int partnerDivision;

        public PartnerListRes(Partner partner) {
            this.partnerId = partner.getPartnerId();
            this.partnerName = partner.getPartnerName();
            this.partnerDetail = partner.getPartnerDetail();
            this.partnerBirth = partner.getPartnerBirth();
            this.partnerDivision = partner.getPartnerDivision();
        }
    }
}
