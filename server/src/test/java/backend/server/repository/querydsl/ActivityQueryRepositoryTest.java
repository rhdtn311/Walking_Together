package backend.server.repository.querydsl;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ActivityQueryRepositoryTest {

    @Autowired
    ActivityQueryRepository activityQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("활동 중인 멤버 확인 테스트")
    void isActive() {

        // given
        Member notActiveMember = Member.builder()
                .name("notActiveMember")
                .birth("19960311")
                .stdId("2015100885")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01000000000")
                .build();

        userRepository.save(notActiveMember);

        Member activeMember = Member.builder()
                .name("activeMember")
                .birth("19960311")
                .stdId("2015100800")
                .password("password")
                .department("학과1")
                .email("email2@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(activeMember);

        Partner notActiveMemberPartner = Partner.builder()
                .partnerName("notActiveMemberPartner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(notActiveMember)
                .build();

        partnerRepository.save(notActiveMemberPartner);

        Partner activeMemberPartner = Partner.builder()
                .partnerName("activeMemberPartner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason2")
                .gender("남성")
                .member(activeMember)
                .build();

        partnerRepository.save(activeMemberPartner);

        // when
        Activity notActiveActivity = Activity.builder()
                .member(notActiveMember)
                .partner(notActiveMemberPartner)
                .activityStatus(0)
                .build();

        activityRepository.save(notActiveActivity);

        Activity activeActivity = Activity.builder()
                .member(activeMember)
                .partner(activeMemberPartner)
                .activityStatus(1)
                .build();

        activityRepository.save(activeActivity);

        // then
        assertTrue(activityQueryRepository.existsActiveActivity("2015100800"));
        assertFalse(activityQueryRepository.existsActiveActivity("2015100885"));
    }
}