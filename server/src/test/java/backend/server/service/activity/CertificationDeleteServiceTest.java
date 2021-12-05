package backend.server.service.activity;

import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.CertificationRepository;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
class CertificationDeleteServiceTest {

    @Autowired
    CertificationDeleteService certificationDeleteService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Test
    @DisplayName("활동id로 인증서가 삭제되는지 확인")
    void deleteCertification() {

        // given
        Certification certification = certificationRepository.save(Certification.builder()
                .certificationId(1L)
                .stdId("stdID")
                .name("Kong")
                .department("department")
                .distance(100L)
                .partnerName("parnter")
                .activityDate(LocalDate.of(2021, 12, 31))
                .careTime(LocalTime.of(0, 0))
                .ordinaryTime(LocalTime.now())
                .startTime(LocalDateTime.of(2021, 12, 31, 9, 0))
                .endTime(LocalDateTime.of(2021, 12, 31, 12, 0))
                .build());

        // when
        certificationDeleteService.deleteCertification(certification.getCertificationId());


        // then
        assertThat(certificationRepository.findById(certification.getCertificationId())).isNotPresent();
    }

}