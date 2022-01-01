package backend.server.repository.querydsl;

import backend.server.DTO.admin.AdminDTO;
import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class AdminQueryRepositoryTest {

    @Autowired
    AdminQueryRepository adminQueryRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PartnerRepository partnerRepository;

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

        userRepository.save(member1);
        userRepository.save(member2);

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
                .build();

        activity2 = Activity.builder()
                .member(member2)
                .partner(partner2)
                .activityDivision(1)
                .activityDate(LocalDate.of(2022, 5, 1))
                .startTime(LocalDateTime.of(2022, 5, 1, 10, 0, 0))
                .endTime(LocalDateTime.of(2022, 5, 1, 12, 0, 0))
                .build();

        activityRepository.save(activity1);
        activityRepository.save(activity2);

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
        assertThat(findActivity1.getTotalDistance()).isSameAs(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivity2.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivity2.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivity2.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivity2.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity2.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getTotalDistance()).isSameAs(activity2.getDistance());
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
        assertThat(findActivity1.getTotalDistance()).isSameAs(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivity2.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivity2.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivity2.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivity2.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivity2.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivity2.getTotalDistance()).isSameAs(activity2.getDistance());
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
        assertThat(findActivityDivision0.getTotalDistance()).isSameAs(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findActivityDivision1.getStdName()).isEqualTo(member2.getName());
        assertThat(findActivityDivision1.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findActivityDivision1.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findActivityDivision1.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findActivityDivision1.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision1.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findActivityDivision1.getTotalDistance()).isSameAs(activity2.getDistance());
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
        assertThat(findFromA1To.getTotalDistance()).isSameAs(activity1.getDistance());

        assertThat(findActivityList2.size()).isEqualTo(1);
        assertThat(findFromA2To.getStdName()).isEqualTo(member2.getName());
        assertThat(findFromA2To.getDepartment()).isEqualTo(member2.getDepartment());
        assertThat(findFromA2To.getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(findFromA2To.getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(findFromA2To.getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA2To.getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(findFromA2To.getTotalDistance()).isSameAs(activity2.getDistance());
    }
}