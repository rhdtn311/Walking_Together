package backend.server.service.feed;

import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.CertificationRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FeedCertificationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private FeedCertificationService feedCertificationService;

    Member member;
    Partner partner;
    Activity activity1;
    Activity activity2;
    Activity activity3;
    Activity activity4;

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

        partner = Partner.builder()
                .member(member)
                .partnerName("ordinaryPartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();
        partnerRepository.save(partner);

        // 일반 활동 : 3시간
        activity1 = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(5000L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 3, 1))
                .startTime(LocalDateTime.of(2022, 3, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2022, 3, 1, 15, 0, 0))
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(3, 0))
                .careTime(LocalTime.of(0, 0))
                .build();
        activityRepository.save(activity1);

        // 일반 활동 : 2시간
        activity2 = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(4420L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2021, 12, 31))
                .startTime(LocalDateTime.of(2021, 12, 31, 12, 0, 0))
                .endTime(LocalDateTime.of(2021, 12, 31, 14, 0, 0))
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(2, 0))
                .careTime(LocalTime.of(0, 0))
                .build();
        activityRepository.save(activity2);

        // 돌봄 활동 : 30분
        activity3 = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(4521L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 5, 20))
                .startTime(LocalDateTime.of(2022, 5, 20, 12, 0, 0))
                .endTime(LocalDateTime.of(2022, 5, 20, 12, 31, 0))
                .activityDivision(1)
                .ordinaryTime(LocalTime.of(0, 0))
                .careTime(LocalTime.of(0, 30))
                .build();
        activityRepository.save(activity3);

        // 돌봄 활동 : 5시간
        activity4 = Activity.builder()
                .member(member)
                .distance(4420L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 5, 21))
                .startTime(LocalDateTime.of(2022, 5, 21, 12, 0, 0))
                .endTime(LocalDateTime.of(2022, 5, 21, 17, 0, 0))
                .activityDivision(1)
                .ordinaryTime(LocalTime.of(0, 0))
                .careTime(LocalTime.of(5, 0))
                .build();
        activityRepository.save(activity4);

        Certification certification1 = Certification.builder()
                .certificationId(activity1.getActivityId())
                .stdId(member.getStdId())
                .activityDate(activity1.getActivityDate())
                .partnerName(partner.getPartnerName())
                .department(member.getDepartment())
                .name(member.getName())
                .startTime(activity1.getStartTime())
                .endTime(activity1.getEndTime())
                .ordinaryTime(activity1.getOrdinaryTime())
                .careTime(activity1.getCareTime())
                .distance(activity1.getDistance())
                .build();

        certificationRepository.save(certification1);

        Certification certification2 = Certification.builder()
                .certificationId(activity2.getActivityId())
                .stdId(member.getStdId())
                .activityDate(activity2.getActivityDate())
                .partnerName(partner.getPartnerName())
                .department(member.getDepartment())
                .name(member.getName())
                .startTime(activity2.getStartTime())
                .endTime(activity2.getEndTime())
                .ordinaryTime(activity2.getOrdinaryTime())
                .careTime(activity2.getCareTime())
                .distance(activity2.getDistance())
                .build();

        certificationRepository.save(certification2);

        Certification certification3 = Certification.builder()
                .certificationId(activity3.getActivityId())
                .stdId(member.getStdId())
                .activityDate(activity3.getActivityDate())
                .partnerName(partner.getPartnerName())
                .department(member.getDepartment())
                .name(member.getName())
                .startTime(activity3.getStartTime())
                .endTime(activity3.getEndTime())
                .ordinaryTime(activity3.getOrdinaryTime())
                .careTime(activity3.getCareTime())
                .distance(activity3.getDistance())
                .build();

        certificationRepository.save(certification3);

        Certification certification4 = Certification.builder()
                .certificationId(activity4.getActivityId())
                .stdId(member.getStdId())
                .activityDate(activity4.getActivityDate())
                .partnerName(partner.getPartnerName())
                .department(member.getDepartment())
                .name(member.getName())
                .startTime(activity4.getStartTime())
                .endTime(activity4.getEndTime())
                .ordinaryTime(activity4.getOrdinaryTime())
                .careTime(activity4.getCareTime())
                .distance(activity4.getDistance())
                .build();

        certificationRepository.save(certification4);

    }

    @Test
    @DisplayName("지정한 날짜 사이에 존재하는 활동만 조회하는지 확인")
    void findBetweenFromTo() {

        LocalDate from = LocalDate.of(2022, 3, 1);
        LocalDate to = LocalDate.of(2022, 5, 20);
        // when
        FeedDTO.CertificationResDTO certification = feedCertificationService.getCertification(from, to, "2015100885");

        // then
        assertThat(certification.getEachCertificationInfos().size()).isEqualTo(2);

        // activity1, activity3이 조회 대상
        List<CertificationDTO> certificationInfos = certification.getEachCertificationInfos();
        for (int i = 0; i < certificationInfos.size(); i++) {
            LocalDate activityDate = certificationInfos.get(i).getActivityDate();
            assertTrue(((activityDate.isEqual(from)) || activityDate.isAfter(from)) &&
                    (activityDate.isEqual(to) || activityDate.isBefore(to)));
        }
    }

    @Test
    @DisplayName("조회된 활동들의 활동 시간 합이 일치하는지 확인")
    void checkActivitiesSumOfTimes() {

        // given

        // 모든 활동 조회
        LocalDate from = LocalDate.of(2021, 3, 1);
        LocalDate to = LocalDate.of(2023, 5, 20);

        // 돌봄 활동의 모든 시간의 합
        String sumOfCareTime = "05:30";

        // 돌봄 활동의 모든 시간의 합
        String sumOfOrdinaryTime = "05:00";

        // 모든 시간의 합
        String totalTime = "10:30";

        // when
        FeedDTO.CertificationResDTO certification = feedCertificationService.getCertification(from, to, "2015100885");

        assertThat(certification.getCareTimes()).isEqualTo(sumOfCareTime);
        assertThat(certification.getOrdinaryTimes()).isEqualTo(sumOfOrdinaryTime);
        assertThat(certification.getTotalTime()).isEqualTo(totalTime);
    }

}