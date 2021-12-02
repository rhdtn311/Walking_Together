package backend.server.service.activity;

import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.CertificationRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CertificationSaveServiceTest {

    @Autowired
    CertificationSaveService certificationSaveService;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Test
    @DisplayName("활동, 회원이 주어졌을 때 인증서가 제대로 저장되는지 확인 : 일반 활동일 경우")
    void certificationSaveOrdinaryActivity() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .name("Kong")
                .department("department")
                .email("email@naver.com")
                .password("password")
                .build();

        userRepository.save(member);

        Activity activity = Activity.builder()
                .partner(Partner.builder().partnerName("partner").build())
                .activityDate(LocalDate.of(2021, 12, 31))
                .distance(5000L)
                .startTime(LocalDateTime.of(2021, 12, 31, 9, 0, 0))
                .endTime(LocalDateTime.of(2021, 12, 31, 12, 0, 0))
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(3, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(activity);

        // when
        Long id = certificationSaveService.saveCertification(member, activity);

        Certification certification = certificationRepository.findById(id).get();
        Activity findActivity = activityRepository.findById(savedActivity.getActivityId()).get();

        // then
        assertThat(certification.getCertificationId()).isEqualTo(findActivity.getActivityId());
        assertThat(certification.getActivityId()).isEqualTo(findActivity.getActivityId());
        assertThat(certification.getActivityDate()).isEqualTo(activity.getActivityDate());
        assertThat(certification.getDistance()).isEqualTo(activity.getDistance());
        assertThat(certification.getCareTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(certification.getOrdinaryTime()).isEqualTo(activity.getOrdinaryTime());
        assertThat(certification.getStartTime()).isEqualTo(activity.getStartTime());
        assertThat(certification.getEndTime()).isEqualTo(activity.getEndTime());
        assertThat(certification.getPartnerName()).isEqualTo(activity.getPartner().getPartnerName());

        assertThat(certification.getStdId()).isEqualTo(member.getStdId());
        assertThat(certification.getName()).isEqualTo(member.getName());
        assertThat(certification.getDepartment()).isEqualTo(member.getDepartment());
    }

    @Test
    @DisplayName("활동, 회원이 주어졌을 때 인증서가 제대로 저장되는지 확인 : 돌봄 활동일 경우")
    void certificationSaveCareActivity() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .name("Kong")
                .department("department")
                .email("email@naver.com")
                .password("password")
                .build();

        userRepository.save(member);

        Activity activity = Activity.builder()
                .partner(Partner.builder().partnerName("partner").build())
                .activityDate(LocalDate.of(2021, 12, 31))
                .distance(5000L)
                .startTime(LocalDateTime.of(2021, 12, 31, 9, 0, 0))
                .endTime(LocalDateTime.of(2021, 12, 31, 12, 0, 0))
                .activityDivision(1)
                .ordinaryTime(LocalTime.of(0, 0))
                .careTime(LocalTime.of(3,0))
                .build();

        Activity savedActivity = activityRepository.save(activity);

        // when
        Long id = certificationSaveService.saveCertification(member, activity);

        Certification certification = certificationRepository.findById(id).get();
        Activity findActivity = activityRepository.findById(savedActivity.getActivityId()).get();

        // then
        assertThat(certification.getCertificationId()).isEqualTo(findActivity.getActivityId());
        assertThat(certification.getActivityId()).isEqualTo(findActivity.getActivityId());
        assertThat(certification.getActivityDate()).isEqualTo(activity.getActivityDate());
        assertThat(certification.getDistance()).isEqualTo(activity.getDistance());
        assertThat(certification.getOrdinaryTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(certification.getCareTime()).isEqualTo(activity.getCareTime());
        assertThat(certification.getStartTime()).isEqualTo(activity.getStartTime());
        assertThat(certification.getEndTime()).isEqualTo(activity.getEndTime());
        assertThat(certification.getPartnerName()).isEqualTo(activity.getPartner().getPartnerName());

        assertThat(certification.getStdId()).isEqualTo(member.getStdId());
        assertThat(certification.getName()).isEqualTo(member.getName());
        assertThat(certification.getDepartment()).isEqualTo(member.getDepartment());
    }

}