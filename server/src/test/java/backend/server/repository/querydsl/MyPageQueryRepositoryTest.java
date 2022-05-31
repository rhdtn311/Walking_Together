package backend.server.repository.querydsl;

import backend.TestConfig;
import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.*;
import backend.server.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({TestConfig.class, MyPageQueryRepository.class})
@DataJpaTest
class MyPageQueryRepositoryTest {

    @Autowired
    private MyPageQueryRepository myPageQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    Member member1, member2;
    Partner partner1, partner2;

    @BeforeEach
    void init() {
        member1 = Member.builder()
                .stdId("stdId1")
                .name("name1")
                .password("password1")
                .department("department1")
                .email("email1")
                .birth("birth1")
                .phoneNumber("phoneNumber1")
                .distance(1000L)
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .stdId("stdId2")
                .name("name2")
                .password("password2")
                .email("email2")
                .birth("birth2")
                .department("department2")
                .phoneNumber("phoneNumber2")
                .distance(2000L)
                .build();
        memberRepository.save(member2);

        partner1 = Partner.builder()
                .member(member1)
                .partnerName("partnerName1")
                .partnerBirth("partnerBirth1")
                .partnerDivision(0)
                .partnerDetail("partnerDetail1")
                .gender("gender1")
                .partnerPhoto("partnerPhoto1")
                .selectionReason("selectionReason1")
                .relationship("relationship1")
                .build();
        partnerRepository.save(partner1);

        partner2 = Partner.builder()
                .member(member1)
                .partnerName("partnerName2")
                .partnerBirth("partnerBirth2")
                .partnerDivision(1)
                .partnerDetail("partnerDetail2")
                .gender("gender2")
                .partnerPhoto("partnerPhoto2")
                .selectionReason("selectionReason2")
                .relationship("relationship2")
                .build();
        partnerRepository.save(partner2);
    }

    @Test
    @DisplayName("파트너 정보 조회")
    void findPartnersInfo() {
        // given
        String stdId = member1.getStdId();

        // when
        List<MyPageDTO.MyPagePartnerListResDTO> myPagePartnerListResDTOs = myPageQueryRepository.findPartnersInfo(stdId);

        // then
        assertThat(myPagePartnerListResDTOs.size()).isEqualTo(2);
        assertThat(myPagePartnerListResDTOs.get(0).getPartnerId()).isEqualTo(partner1.getPartnerId());
        assertThat(myPagePartnerListResDTOs.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(myPagePartnerListResDTOs.get(0).getPartnerBirth()).isEqualTo(partner1.getPartnerBirth());
        assertThat(myPagePartnerListResDTOs.get(0).getPartnerDetail()).isEqualTo(partner1.getPartnerDetail());
    }
}