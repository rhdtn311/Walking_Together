package backend.server.service.activity;

import backend.server.DTO.activity.PartnerDTO;
import backend.server.entity.Partner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ActivityCreationUnitTest {

    private ActivityCreationService activityCreationService
            = new ActivityCreationService(null, null, null, null, null, null);

    @Test
    @DisplayName("파트너 리스트가 DTO로 바뀌는지 확인")
    void partnerListToPartnerListResDTO() {

        // given
        Partner partner1 = Partner.builder()
                .partnerId(1L)
                .build();

        Partner partner2 = Partner.builder()
                .partnerId(2L)
                .build();

        Partner partner3 = Partner.builder()
                .partnerId(3L)
                .build();

        List<Partner> partnerList = new ArrayList<>(List.of(partner1, partner2, partner3));

        // when
        List<PartnerDTO.PartnerListRes> partnerDTOList = activityCreationService.createPartnerList(partnerList);

        // then
        Assertions.assertThat(partnerDTOList.size()).isEqualTo(3);
        Assertions.assertThat(partnerDTOList.get(0).getPartnerId()).isEqualTo(1L);
        Assertions.assertThat(partnerDTOList.get(1).getPartnerId()).isEqualTo(2L);
        Assertions.assertThat(partnerDTOList.get(2).getPartnerId()).isEqualTo(3L);

    }
}
