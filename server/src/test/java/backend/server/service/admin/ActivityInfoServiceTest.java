package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.DTO.common.MapCaptureDTO;
import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.repository.querydsl.AdminQueryRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ActivityInfoServiceTest {

    @Autowired
    AdminQueryRepository adminQueryRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    MapCaptureRepository mapCaptureRepository;

    @Autowired
    ActivityInfoService activityInfoService;

    @PersistenceContext
    private EntityManager em;

    static Member member1;
    static Member member2;
    static Activity activity1;
    static Activity activity2;
    static Partner partner1;
    static Partner partner2;

    @BeforeEach
    void init() {
        // given
        member1 = Member.builder()
                .name("Kong")
                .stdId("2015100885")
                .email("rhdtn311@naver.com")
                .password("password")
                .department("컴퓨터")
                .distance(5000L)
                .build();

        member2 = Member.builder()
                .name("Kim")
                .stdId("2015100276")
                .email("rhdwk987@naver.com")
                .password("password")
                .department("디자인")
                .distance(6000L)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        partner1 = Partner.builder()
                .member(member1)
                .partnerName("KongPartner")
                .partnerDetail("o")
                .partnerDivision(0)
                .build();

        partner2 = Partner.builder()
                .member(member2)
                .partnerName("KimPartner")
                .partnerDetail("c")
                .partnerDivision(1)
                .build();

        partnerRepository.save(partner1);
        partnerRepository.save(partner2);

        activity1 = Activity.builder()
                .member(member1)
                .partner(partner1)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 1, 1))
                .startTime(LocalDateTime.of(2022, 1, 1, 10, 0, 0))
                .endTime(LocalDateTime.of(2022, 1, 1, 12, 0, 0))
                .review("좋은 활동1이었습니다.")
                .distance(5000L)
                .build();

        activity2 = Activity.builder()
                .member(member2)
                .partner(partner2)
                .activityDivision(1)
                .activityDate(LocalDate.of(2022, 5, 1))
                .startTime(LocalDateTime.of(2022, 5, 1, 10, 0, 0))
                .endTime(LocalDateTime.of(2022, 5, 1, 12, 0, 0))
                .review("좋은 활동2였습니다.")
                .distance(6000L)
                .build();

        Activity savedActivity1 = activityRepository.save(activity1);
        Activity savedActivity2 = activityRepository.save(activity2);

        for (int i = 0; i < 2; i++) {
            List<String> latList = new ArrayList<>();
            List<String> lonList = new ArrayList<>();
            List<String> timestampList = new ArrayList<>();

            createMapCaptures(latList, lonList, timestampList);

            if (i == 0) {
                for (int j = 0; j < 10; j++) {
                    MapCapture mapCapture = MapCapture.builder()
                            .activity(savedActivity1)
                            .lat(latList.get(j))
                            .lon(lonList.get(j))
                            .timestamp(timestampList.get(j))
                            .build();

                    mapCaptureRepository.save(mapCapture);
                }

            } else {
                for (int j = 0; j < 10; j++) {
                    MapCapture mapCapture = MapCapture.builder()
                            .activity(savedActivity2)
                            .lat(latList.get(j))
                            .lon(lonList.get(j))
                            .timestamp(timestampList.get(j))
                            .build();

                    mapCaptureRepository.save(mapCapture);
                }
            }
        }
    }

    double makeNumber() {
        return Math.random();
    }

    void createMapCaptures(List<String> latList, List<String> lonList, List<String> timestampList) {
        // MapCapture 저장

        // 무작위로 lat, lon, timestamp를 각각 10개씩 생성 후 각각 검증에 사용할 list에 넣어줌
        for (int i = 0; i < 30; i++) {
            if (i >= 0 && i < 10) {
                String lat = String.valueOf(makeNumber() * 100);
                latList.add(lat);
            } else if (i >= 10 && i < 20) {
                String lon = String.valueOf(makeNumber() * 100);
                lonList.add(lon);
            } else {
                String timestamp = String.valueOf((long) (makeNumber() * 10000000000000L));
                timestampList.add(timestamp);
            }
        }
    }


    @Test
    @DisplayName("활동을 한 회원의 name이 keyword와 같은 경우 ")
    void findActivityInfoKeywordName() {

        // when
        AdminDTO.ActivityInfoReqDTO kongKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("Kong")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(kongKeyword);
        AdminDTO.ActivityInfoResDTO findActivity1 = findActivityList1.get(0);

        AdminDTO.ActivityInfoReqDTO kimKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("Kim")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList2 = adminQueryRepository.findActivityInfo(kimKeyword);
        AdminDTO.ActivityInfoResDTO findActivity2 = findActivityList2.get(0);

        // then
        assertThat(findActivityList1.size()).isEqualTo(1);
        assertThat(findActivity1.getStdName()).isEqualTo(member1.getName());
        assertThat(findActivity1.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findActivity1.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(findActivity1.getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity1.getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity1.getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity1.getTotalDistance()).isEqualTo(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivity2.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivity2.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivity2.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivity2.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity2.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getTotalDistance()).isEqualTo(activity2.getDistance());
    }

    @Test
    @DisplayName("활동을 한 회원의 name이 여러 개 중복되는 경우")
    void findActivitiesInfoKeywordName() {
        // when
        AdminDTO.ActivityInfoReqDTO duplicationKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("K")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(duplicationKeyword);

        assertThat(findActivityList1.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("활동을 한 회원의 stdId가 keyword와 같은 경우")
    void findActivityInfoKeywordStdId() {
        // when
        AdminDTO.ActivityInfoReqDTO kongKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015100885")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(kongKeyword);
        AdminDTO.ActivityInfoResDTO findActivity1 = findActivityList1.get(0);

        AdminDTO.ActivityInfoReqDTO kimKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015100276")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList2 = adminQueryRepository.findActivityInfo(kimKeyword);
        AdminDTO.ActivityInfoResDTO findActivity2 = findActivityList2.get(0);

        // then
        assertThat(findActivityList1.size()).isEqualTo(1);
        assertThat(findActivity1.getStdName()).isEqualTo(member1.getName());
        assertThat(findActivity1.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findActivity1.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(findActivity1.getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity1.getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity1.getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity1.getTotalDistance()).isEqualTo(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivity2.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivity2.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivity2.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivity2.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity2.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getTotalDistance()).isEqualTo(activity2.getDistance());
    }

    @Test
    @DisplayName("활동을 한 회원의 stdId가 여러 개 중복되는 경우")
    void findActivitiesInfoKeywordStdId() {
        // when
        AdminDTO.ActivityInfoReqDTO duplicationKeyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(duplicationKeyword);

        assertThat(findActivityList1.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 활동의 activityDivision과 검색하기 위해 파라미터로 받는 acitivityDivision이 같은 경우")
    void findActivityInfoByActivityDivision() {

        AdminDTO.ActivityInfoReqDTO activityDivision0Keyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(0)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(activityDivision0Keyword);
        AdminDTO.ActivityInfoResDTO findActivityDivision0 = findActivityList1.get(0);

        AdminDTO.ActivityInfoReqDTO activityDivision1Keyword = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015")
                .from("2000/01/01")
                .to("3000/01/01")
                .activityDivision(1)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList2 = adminQueryRepository.findActivityInfo(activityDivision1Keyword);
        AdminDTO.ActivityInfoResDTO findActivityDivision1 = findActivityList2.get(0);

        // then
        assertThat(findActivityList1.size()).isEqualTo(1);
        assertThat(findActivityDivision0.getStdName()).isEqualTo(member1.getName());
        assertThat(findActivityDivision0.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findActivityDivision0.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(findActivityDivision0.getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivityDivision0.getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision0.getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision0.getTotalDistance()).isEqualTo(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivityDivision1.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivityDivision1.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivityDivision1.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivityDivision1.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivityDivision1.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision1.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision1.getTotalDistance()).isEqualTo(activity2.getDistance());
    }

    @Test
    @DisplayName("특정 활동의 activityDate가 파라미터로 받는 from과 to의 사이에 존재할 경우")
    void findActivityInfoBetweenFromTo() {
        AdminDTO.ActivityInfoReqDTO findFromActivity1To = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015")
                .from("2022/01/01")
                .to("2022/01/02")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList1 = adminQueryRepository.findActivityInfo(findFromActivity1To);
        AdminDTO.ActivityInfoResDTO findFromA1To = findActivityList1.get(0);

        AdminDTO.ActivityInfoReqDTO findFromActivity2To = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("2015")
                .from("2022/04/01")
                .to("2022/05/01")
                .activityDivision(2)
                .build();

        List<AdminDTO.ActivityInfoResDTO> findActivityList2 = adminQueryRepository.findActivityInfo(findFromActivity2To);
        AdminDTO.ActivityInfoResDTO findFromA2To = findActivityList2.get(0);

        // then
        assertThat(findActivityList1.size()).isEqualTo(1);
        assertThat(findFromA1To.getStdName()).isEqualTo(member1.getName());
        assertThat(findFromA1To.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findFromA1To.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(findFromA1To.getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findFromA1To.getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA1To.getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA1To.getTotalDistance()).isEqualTo(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findFromA2To.getStdName()).isEqualTo(member2.getName());
        assertThat(findFromA2To.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findFromA2To.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findFromA2To.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findFromA2To.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA2To.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA2To.getTotalDistance()).isEqualTo(activity2.getDistance());
    }

    @Test
    @DisplayName("활동 세부 조회 시 정상적으로 조회되는지 확인")
    void findActivityDetailInfo() {

        // when
        em.clear();
        Activity findActivity1 = activityRepository.findById(activity1.getActivityId()).get();
        Activity findActivity2 = activityRepository.findById(activity2.getActivityId()).get();

        List<MapCapture> activity1MapCaptures = mapCaptureRepository.findAllByActivity(findActivity1);
        List<MapCapture> activity2MapCaptures = mapCaptureRepository.findAllByActivity(findActivity2);

        AdminDTO.ActivityDetailInfoResDTO activity1DetailInfo = adminQueryRepository.findActivityDetailInfo(findActivity1.getActivityId());
        AdminDTO.ActivityDetailInfoResDTO activity2DetailInfo = adminQueryRepository.findActivityDetailInfo(findActivity2.getActivityId());
        activity1DetailInfo.setMapPicture(MapCaptureDTO.MapCaptureResDTO.toDTOList(findActivity1.getMapCaptures()));
        activity2DetailInfo.setMapPicture(MapCaptureDTO.MapCaptureResDTO.toDTOList(findActivity2.getMapCaptures()));

        // then
        assertThat(activity1DetailInfo.getStdName()).isEqualTo(member1.getName());
        assertThat(activity1DetailInfo.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(activity1DetailInfo.getStdId()).isEqualTo(member1.getStdId());
        assertThat(activity1DetailInfo.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(activity1DetailInfo.getReview()).isEqualTo(findActivity1.getReview());
        assertThat(activity1DetailInfo.getActivityDate()).isEqualTo(findActivity1.getActivityDate());
        assertThat(activity1DetailInfo.getStartTime()).isEqualTo(findActivity1.getStartTime());
        assertThat(activity1DetailInfo.getEndTime()).isEqualTo(findActivity1.getEndTime());
        assertThat(activity1DetailInfo.getTotalDistance()).isEqualTo(findActivity1.getDistance());

        List<MapCaptureDTO.MapCaptureResDTO> mapPicture1 = activity1DetailInfo.getMapPicture();
        for (int i = 0; i < 10; i++) {

            assertThat(mapPicture1.get(i).getLat()).isEqualTo(activity1MapCaptures.get(i).getLat());
            assertThat(mapPicture1.get(i).getLon()).isEqualTo(activity1MapCaptures.get(i).getLon());
            assertThat(mapPicture1.get(i).getTimestamp()).isEqualTo(activity1MapCaptures.get(i).getTimestamp());

        }

        assertThat(activity2DetailInfo.getStdName()).isEqualTo(member2.getName());
        assertThat(activity2DetailInfo.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(activity2DetailInfo.getStdId()).isEqualTo(member2.getStdId());
        assertThat(activity2DetailInfo.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(activity2DetailInfo.getReview()).isEqualTo(findActivity2.getReview());
        assertThat(activity2DetailInfo.getActivityDate()).isEqualTo(findActivity2.getActivityDate());
        assertThat(activity2DetailInfo.getStartTime()).isEqualTo(findActivity2.getStartTime());
        assertThat(activity2DetailInfo.getEndTime()).isEqualTo(findActivity2.getEndTime());
        assertThat(activity2DetailInfo.getTotalDistance()).isEqualTo(findActivity2.getDistance());

        List<MapCaptureDTO.MapCaptureResDTO> mapPicture2 = activity2DetailInfo.getMapPicture();
        for (int i = 0; i < 10; i++) {

            assertThat(mapPicture2.get(i).getLat()).isEqualTo(activity2MapCaptures.get(i).getLat());
            assertThat(mapPicture2.get(i).getLon()).isEqualTo(activity2MapCaptures.get(i).getLon());
            assertThat(mapPicture2.get(i).getTimestamp()).isEqualTo(activity2MapCaptures.get(i).getTimestamp());
        }
    }

    @Test
    @DisplayName("비즈니스 예외 : 조회하려는 활동이 없을 경우 ActivityNotFoundException 처리 확인")
    void activityNotFoundException() {

        assertThrows(ActivityNotFoundException.class, () ->
                activityInfoService.findActivityDetailInfo(0L)
        );
    }
}