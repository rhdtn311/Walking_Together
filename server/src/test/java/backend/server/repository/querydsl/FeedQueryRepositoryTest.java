package backend.server.repository.querydsl;

import backend.TestConfig;
import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.common.MapCaptureDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.*;
import backend.server.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({TestConfig.class, FeedQueryRepository.class})
@DataJpaTest
class FeedQueryRepositoryTest {

    @Autowired
    private FeedQueryRepository feedQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private MapCaptureRepository mapCaptureRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    Member member1, member2;
    Partner partner1, partner2;
    Activity activity1, activity2, activity3;
    MapCapture mapCapture1, mapCapture2;
    Certification certification1, certification2, certification3;

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
                .member(member2)
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

        activity1 = Activity.builder()
                .member(member1)
                .partner(partner1)
                .review("review1")
                .activityDate(LocalDate.of(2022,5,1))
                .startTime(LocalDateTime.of(2022, 5, 1, 0, 0))
                .endTime(LocalDateTime.of(2022, 5, 1, 12, 0))
                .careTime(LocalTime.of(12, 0))
                .distance(1000L)
                .activityDivision(0)
                .build();
        activityRepository.save(activity1);

        activity2 = Activity.builder()
                .member(member2)
                .partner(partner2)
                .review("review2")
                .activityDate(LocalDate.of(2022,12,15))
                .startTime(LocalDateTime.of(2022, 12, 15, 0, 0))
                .endTime(LocalDateTime.of(2022, 12, 15, 3, 0))
                .careTime(LocalTime.of(3, 0))
                .distance(2000L)
                .activityDivision(1)
                .build();
        activityRepository.save(activity2);

        activity3 = Activity.builder()
                .member(member2)
                .partner(partner2)
                .review("review3")
                .activityDate(LocalDate.of(2022,10,15))
                .startTime(LocalDateTime.of(2022, 10, 15, 0, 0))
                .endTime(LocalDateTime.of(2022, 10, 15, 3, 0))
                .careTime(LocalTime.of(3, 0))
                .distance(2000L)
                .activityDivision(1)
                .build();
        activityRepository.save(activity3);

        mapCapture1 = MapCapture.builder()
                .activityId(activity1.getActivityId())
                .lat("lat1")
                .lon("lon1")
                .timestamp("timestamp1")
                .build();
        mapCaptureRepository.save(mapCapture1);

        mapCapture2 = MapCapture.builder()
                .activityId(activity2.getActivityId())
                .lat("lat2")
                .lon("lon2")
                .timestamp("timestamp2")
                .build();
        mapCaptureRepository.save(mapCapture2);

        certification1 = Certification.builder()
                .certificationId(activity1.getActivityId())
                .activityDate(activity1.getActivityDate())
                .partnerName(partner1.getPartnerName())
                .department(member1.getDepartment())
                .name(member1.getName())
                .startTime(activity1.getStartTime())
                .ordinaryTime(activity1.getOrdinaryTime())
                .careTime(activity1.getCareTime())
                .endTime(activity1.getEndTime())
                .distance(activity1.getDistance())
                .stdId(member1.getStdId())
                .activityId(activity1.getActivityId())
                .build();
        certificationRepository.save(certification1);

        certification2 = Certification.builder()
                .certificationId(activity2.getActivityId())
                .activityDate(activity2.getActivityDate())
                .partnerName(partner2.getPartnerName())
                .department(member2.getDepartment())
                .name(member2.getName())
                .startTime(activity2.getStartTime())
                .ordinaryTime(activity2.getOrdinaryTime())
                .careTime(activity2.getCareTime())
                .endTime(activity2.getEndTime())
                .distance(activity2.getDistance())
                .stdId(member2.getStdId())
                .activityId(activity2.getActivityId())
                .build();
        certificationRepository.save(certification2);

        certification3 = Certification.builder()
                .certificationId(activity3.getActivityId())
                .activityDate(activity3.getActivityDate())
                .partnerName(partner2.getPartnerName())
                .department(member2.getDepartment())
                .name(member2.getName())
                .startTime(activity3.getStartTime())
                .ordinaryTime(activity3.getOrdinaryTime())
                .careTime(activity3.getCareTime())
                .endTime(activity3.getEndTime())
                .distance(activity3.getDistance())
                .stdId(member2.getStdId())
                .activityId(activity3.getActivityId())
                .build();
        certificationRepository.save(certification3);
    }

    @Test
    @DisplayName("피드 메인 조회")
    void findFeedMain() {
        // given
        String stdId = member1.getStdId();
        String sort = "asc";

        // when
        List<FeedDTO.FeedMainResDTO> feedMainResDTOs = feedQueryRepository.findFeedMain(stdId, sort);

        // then
        assertThat(feedMainResDTOs.size()).isEqualTo(1);
        assertThat(feedMainResDTOs.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(feedMainResDTOs.get(0).getActivityId()).isEqualTo(activity1.getActivityId());
        assertThat(feedMainResDTOs.get(0).getActivityDivision()).isEqualTo(activity1.getActivityDivision());
        assertThat(feedMainResDTOs.get(0).getActivityStatus()).isEqualTo(activity1.getActivityStatus());
        assertThat(feedMainResDTOs.get(0).getActivityDate()).isEqualTo(activity1.getActivityDate());
    }

    @Test
    @DisplayName("피드 상세 조회")
    void findFeedDetail() {
        // given
        Long activityId = activity1.getActivityId();

        // when
        FeedDTO.FeedDetailResDTO feedDetailResDTO = feedQueryRepository.findFeedDetail(activityId);

        // then
        assertThat(feedDetailResDTO.getActivityDate()).isEqualTo(activity1.getActivityDate());
        assertThat(feedDetailResDTO.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(feedDetailResDTO.getStartTime()).isEqualTo(activity1.getStartTime());
        assertThat(feedDetailResDTO.getEndTime()).isEqualTo(activity1.getEndTime());
        assertThat(feedDetailResDTO.getActivityDivision()).isEqualTo(activity1.getActivityDivision());
        assertThat(feedDetailResDTO.getReview()).isEqualTo(activity1.getReview());
    }

    @Test
    @DisplayName("맵 경로 조회")
    void findMapCapture() {
        // given
        Long activityId = activity1.getActivityId();

        // when
        List<MapCaptureDTO.MapCaptureResDTO> mapCapture = feedQueryRepository.findMapCapture(activityId);

        // then
        assertThat(mapCapture.size()).isEqualTo(1);
        assertThat(mapCapture.get(0).getMapCaptureId()).isEqualTo(mapCapture1.getMapCaptureId());
        assertThat(mapCapture.get(0).getLat()).isEqualTo(mapCapture1.getLat());
        assertThat(mapCapture.get(0).getLon()).isEqualTo(mapCapture1.getLon());
        assertThat(mapCapture.get(0).getTimestamp()).isEqualTo(mapCapture1.getTimestamp());
        assertThat(mapCapture.get(0).getActivityId()).isEqualTo(activity1.getActivityId());
    }

    @Test
    @DisplayName("인증서 조회 - 1개 조회")
    void findCertification() {
        // given
        String stdId = member1.getStdId();
        LocalDate from = LocalDate.of(2022, 5, 1);
        LocalDate to = LocalDate.of(2022, 5, 2);

        // when
        List<CertificationDTO> certificationDTOs = feedQueryRepository.findCertification(from, to, stdId);

        // then
        assertThat(certificationDTOs.size()).isEqualTo(1);
        assertThat(certificationDTOs.get(0).getCertificationId()).isEqualTo(certification1.getCertificationId());
        assertThat(certificationDTOs.get(0).getActivityDate()).isEqualTo(certification1.getActivityDate());
        assertThat(certificationDTOs.get(0).getPartnerName()).isEqualTo(certification1.getPartnerName());
        assertThat(certificationDTOs.get(0).getDepartment()).isEqualTo(certification1.getDepartment());
        assertThat(certificationDTOs.get(0).getName()).isEqualTo(certification1.getName());
        assertThat(certificationDTOs.get(0).getStartTime()).isEqualTo(certification1.getStartTime());
        assertThat(certificationDTOs.get(0).getOrdinaryTime()).isEqualTo(certification1.getOrdinaryTime());
        assertThat(certificationDTOs.get(0).getCareTime()).isEqualTo(certification1.getCareTime());
        assertThat(certificationDTOs.get(0).getEndTime()).isEqualTo(certification1.getEndTime());
        assertThat(certificationDTOs.get(0).getDistance()).isEqualTo(certification1.getDistance());
        assertThat(certificationDTOs.get(0).getStdId()).isEqualTo(certification1.getStdId());
    }

    @Test
    @DisplayName("인증서 조회 - 모두 조회")
    void findAllCertification() {
        // given
        String stdId = member2.getStdId();
        LocalDate from = LocalDate.of(2022, 5, 1);
        LocalDate to = LocalDate.of(2022, 12, 31);

        // when
        List<CertificationDTO> certificationDTOs = feedQueryRepository.findCertification(from, to, stdId);

        // then
        assertThat(certificationDTOs.size()).isEqualTo(2);
    }


}