package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MyPagePartnerInfoServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    MyPagePartnerInfoService myPagePartnerInfoService;

    Member member;
    Partner partner1;
    Partner partner2;

    @BeforeEach
    void init() {
        member = Member.builder()
                .name("김토토")
                .stdId("2015100885")
                .email("rhdtn311@naver.com")
                .password("password")
                .department("컴퓨터")
                .birth("19960311")
                .phoneNumber("01039281329")
                .build();
        userRepository.save(member);

        partner1 = Partner.builder()
                .member(member)
                .partnerName("ordinaryPartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();
        partnerRepository.save(partner1);

        partner2 = Partner.builder()
                .member(member)
                .partnerName("disabledPartner")
                .selectionReason("selectionReason2")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1931-05-13")
                .partnerDetail("d")
                .build();
        partnerRepository.save(partner2);
    }

    @Test
    @DisplayName("학번으로 파트너 리스트 정상 출력 확인")
    void findPartnerListByStdId() {

        // when
        List<MyPageDTO.MyPagePartnerListResDTO> partnerList = myPagePartnerInfoService.findPartnersInfo(member.getStdId());

        // then
        assertThat(partnerList.size()).isEqualTo(2);

        assertThat(partnerList.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(partnerList.get(0).getPartnerBirth()).isEqualTo(partner1.getPartnerBirth());
        assertThat(partnerList.get(0).getPartnerDetail()).isEqualTo(partner1.getPartnerDetail());

        assertThat(partnerList.get(1).getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(partnerList.get(1).getPartnerBirth()).isEqualTo(partner2.getPartnerBirth());
        assertThat(partnerList.get(1).getPartnerDetail()).isEqualTo(partner2.getPartnerDetail());
    }

    @Test
    @DisplayName("파트너 id로 파트너 정상 조회 확인")
    void findPartnerByPartnerId() {

        // when
        MyPageDTO.MyPagePartnerDetailResDTO partnerDetail = myPagePartnerInfoService.findPartnerDetail(partner1.getPartnerId());

        // then
        assertThat(partnerDetail.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(partnerDetail.getPartnerDetail()).isEqualTo(partner1.getPartnerDetail());
        assertThat(partnerDetail.getPartnerBirth()).isEqualTo(partner1.getPartnerBirth());
        assertThat(partnerDetail.getGender()).isEqualTo(partner1.getGender());
        assertThat(partnerDetail.getSelectionReason()).isEqualTo(partner1.getSelectionReason());
        assertThat(partnerDetail.getRelationship()).isEqualTo(partner1.getRelationship());
    }
}