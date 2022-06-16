package backend.server.service.feed;

import backend.server.DTO.common.MapCaptureDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class FeedDetailServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MapCaptureRepository mapCaptureRepository;

    @Autowired
    private FeedDetailService feedDetailService;

    @PersistenceContext
    private EntityManager em;

    Member member;
    Partner partner;
    Activity activity;
    MapCapture[] mapCaptures;

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

        memberRepository.save(member);

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

        activity = Activity.builder()
                .member(member)
                .distance(5000L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 3, 1))
                .startTime(LocalDateTime.of(2022, 3, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2022, 3, 1, 3, 0, 0))
                .build();

        activityRepository.save(activity);

        MapCapture activity1MapCapture1 = MapCapture.builder()
                .activity(activity)
                .lat("1.001")
                .lon("2.222")
                .timestamp("111111")
                .build();
        mapCaptureRepository.save(activity1MapCapture1);

        MapCapture activity1MapCapture2 = MapCapture.builder()
                .activity(activity)
                .lat("1.002")
                .lon("2.001")
                .timestamp("222222")
                .build();
        mapCaptureRepository.save(activity1MapCapture2);

        MapCapture activity1MapCapture3 = MapCapture.builder()
                .activity(activity)
                .lat("1.003")
                .lon("2.002")
                .timestamp("333333")
                .build();
        mapCaptureRepository.save(activity1MapCapture3);

        mapCaptures = new MapCapture[]{activity1MapCapture1, activity1MapCapture2, activity1MapCapture3};
    }

    @Test
    @DisplayName("활동 id로 조회 확인 (MapCaptureList까지)")
    void findByActivityId() {
        // when
        em.clear();;
        Activity findActivity = activityRepository.findById(this.activity.getActivityId()).get();
        FeedDTO.FeedDetailResDTO feedDetail = feedDetailService.getFeedDetail(findActivity.getActivityId());

        // then
        assertThat(feedDetail.getStartTime()).isEqualTo(findActivity.getStartTime());

        List<MapCaptureDTO.MapCaptureResDTO> mapCaptureList = feedDetail.getMapPicture();
        for (int i = 0; i < mapCaptureList.size(); i++) {
            assertThat(mapCaptureList.get(i).getLat()).isEqualTo(mapCaptures[i].getLat());
            assertThat(mapCaptureList.get(i).getLon()).isEqualTo(mapCaptures[i].getLon());
            assertThat(mapCaptureList.get(i).getTimestamp()).isEqualTo(mapCaptures[i].getTimestamp());
        }
    }
}