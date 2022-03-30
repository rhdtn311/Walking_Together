package backend.server.DTO.activity;

import backend.server.entity.Partner;
import lombok.*;

public class PartnerDTO {

    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PartnerListResDTO {

        private Long partnerId;
        private String partnerName;
        private String partnerDetail;
        private String partnerBirth;
        private int partnerDivision;

        public static PartnerListResDTO entityToDto(Partner partner) {
            return PartnerListResDTO.builder()
                    .partnerId(partner.getPartnerId())
                    .partnerName(partner.getPartnerName())
                    .partnerDetail(partner.getPartnerDetail())
                    .partnerBirth(partner.getPartnerBirth())
                    .partnerDivision(partner.getPartnerDivision())
                    .build();
        }
    }
}
