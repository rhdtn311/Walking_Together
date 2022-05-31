package backend.server.repository.querydsl;

import backend.TestConfig;
import backend.server.DTO.admin.AdminDTO;
import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.MemberRepository;
import backend.server.repository.PartnerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestConfig.class, AdminQueryRepository.class})
@DataJpaTest
class AdminQueryRepositoryTest {

    @Autowired
    private AdminQueryRepository adminQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private MapCaptureRepository mapCaptureRepository;

    Member member1, member2;
    Partner partner1, partner2;
    Activity activity1, activity2;
    MapCapture mapCapture1, mapCapture2;

    @BeforeEach
    void init() {

        member1 = Member.builder()
                .name("name1")
                .password("password1")
                .stdId("stdId1")
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

        mapCapture1 = MapCapture.builder()
                .mapCaptureId(1L)
                .activityId(1L)
                .lat("lat1")
                .lon("lon1")
                .timestamp("timestamp1")
                .build();
        mapCaptureRepository.save(mapCapture1);

        mapCapture2 = MapCapture.builder()
                .mapCaptureId(2L)
                .activityId(2L)
                .lat("lat2")
                .lon("lon2")
                .timestamp("timestamp2")
                .build();
        mapCaptureRepository.save(mapCapture2);

    }

    @Test
    @DisplayName("회원 정보 조회 - 이름으로 모두 조회")
    void findMemberInfoByNameTest() {

        // given
        findMemberInfoInit();
        String keyWord = "name";
        List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs = memberInfoResDTOs();


        // when
        List<AdminDTO.MemberInfoResDTO> memberInfos = adminQueryRepository.findMemberInfo(keyWord);

        // then
        for (int i = 0; i < 2; i++) {
            assertThat(memberInfos.get(i).getName()).contains(keyWord);
            assertThat(memberInfos.get(i).getPhoneNumber()).isEqualTo(memberInfoResDTOs.get(i).getPhoneNumber());
            assertThat(memberInfos.get(i).getEmail()).isEqualTo(memberInfoResDTOs.get(i).getEmail());
            assertThat(memberInfos.get(i).getDepartment()).isEqualTo(memberInfoResDTOs.get(i).getDepartment());
            assertThat(memberInfos.get(i).getBirth()).isEqualTo(memberInfoResDTOs.get(i).getBirth());
            assertThat(memberInfos.get(i).getStdId()).isEqualTo(memberInfoResDTOs.get(i).getStdId());
        }
    }

    @Test
    @DisplayName("회원 정보 조회 - 학번으로 모두 조회")
    void findMemberInfoByStdIdTest() {

        // given
        findMemberInfoInit();
        String keyWord = "stdId";
        List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs = memberInfoResDTOs();


        // when
        List<AdminDTO.MemberInfoResDTO> memberInfos = adminQueryRepository.findMemberInfo(keyWord);

        // then
        for (int i = 0; i < 2; i++) {
            assertThat(memberInfos.get(i).getStdId()).contains(keyWord);
            assertThat(memberInfos.get(i).getPhoneNumber()).isEqualTo(memberInfoResDTOs.get(i).getPhoneNumber());
            assertThat(memberInfos.get(i).getEmail()).isEqualTo(memberInfoResDTOs.get(i).getEmail());
            assertThat(memberInfos.get(i).getDepartment()).isEqualTo(memberInfoResDTOs.get(i).getDepartment());
            assertThat(memberInfos.get(i).getBirth()).isEqualTo(memberInfoResDTOs.get(i).getBirth());
            assertThat(memberInfos.get(i).getName()).isEqualTo(memberInfoResDTOs.get(i).getName());
        }
    }

    @Test
    @DisplayName("회원 정보 조회 - 이름으로 선택(한 개만) 조회")
    void findOneMemberInfoByNameTest() {

        // given
        findMemberInfoInit();
        String keyWord = "1";
        List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs = memberInfoResDTOs();


        // when
        List<AdminDTO.MemberInfoResDTO> memberInfos = adminQueryRepository.findMemberInfo(keyWord);

        // then
        assertThat(memberInfos.size()).isEqualTo(1);
        assertThat(memberInfos.get(0).getStdId()).contains(keyWord);
        assertThat(memberInfos.get(0).getPhoneNumber()).isEqualTo(memberInfoResDTOs.get(0).getPhoneNumber());
        assertThat(memberInfos.get(0).getEmail()).isEqualTo(memberInfoResDTOs.get(0).getEmail());
        assertThat(memberInfos.get(0).getDepartment()).isEqualTo(memberInfoResDTOs.get(0).getDepartment());
        assertThat(memberInfos.get(0).getBirth()).isEqualTo(memberInfoResDTOs.get(0).getBirth());
        assertThat(memberInfos.get(0).getName()).isEqualTo(memberInfoResDTOs.get(0).getName());
    }

    @Test
    @DisplayName("회원 정보 조회 - 학번으로 선택(한 개만) 조회")
    void findOneMemberInfoByStdIdTest() {

        // given
        findMemberInfoInit();
        String keyWord = "2";
        List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs = memberInfoResDTOs();

        // when
        List<AdminDTO.MemberInfoResDTO> memberInfos = adminQueryRepository.findMemberInfo(keyWord);

        // then
        assertThat(memberInfos.size()).isEqualTo(1);
        assertThat(memberInfos.get(0).getStdId()).contains(keyWord);
        assertThat(memberInfos.get(0).getPhoneNumber()).isEqualTo(memberInfoResDTOs.get(1).getPhoneNumber());
        assertThat(memberInfos.get(0).getEmail()).isEqualTo(memberInfoResDTOs.get(1).getEmail());
        assertThat(memberInfos.get(0).getDepartment()).isEqualTo(memberInfoResDTOs.get(1).getDepartment());
        assertThat(memberInfos.get(0).getBirth()).isEqualTo(memberInfoResDTOs.get(1).getBirth());
        assertThat(memberInfos.get(0).getName()).isEqualTo(memberInfoResDTOs.get(1).getName());
    }

    private List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs() {
        List<AdminDTO.MemberInfoResDTO> memberInfoResDTOs = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            memberInfoResDTOs.add(AdminDTO.MemberInfoResDTO.builder()
                    .name("name" + i)
                    .stdId("stdId" + i)
                    .department("department" + i)
                    .email("email" + i)
                    .birth("birth" + i)
                    .phoneNumber("phoneNumber" + i)
                    .build());
        }
        return memberInfoResDTOs;
    }

    private void findMemberInfoInit() {


    }

    @Test
    @DisplayName("활동 정보 조회 - 날짜 사이 조회")
    void findAllActivityInfoBetweenDate() {
        // given
        AdminDTO.ActivityInfoReqDTO activityInfoReqDTO = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword(null)
                .activityDivision(2)
                .from("2022/05/01")
                .to("2022/05/02")
                .build();

        // when
        List<AdminDTO.ActivityInfoResDTO> activityInfo = adminQueryRepository.findActivityInfo(activityInfoReqDTO);

        // then
        assertThat(activityInfo.size()).isEqualTo(1);
        assertThat(activityInfo.get(0).getStdId()).isEqualTo(member1.getStdId());
        assertThat(activityInfo.get(0).getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(activityInfo.get(0).getActivityId()).isEqualTo(activity1.getActivityId());
        assertThat(activityInfo.get(0).getTotalDistance()).isEqualTo(activity1.getDistance());
        assertThat(activityInfo.get(0).getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(activityInfo.get(0).getStdName()).isEqualTo(member1.getName());
    }

    @Test
    @DisplayName("활동 정보 조회 - 날짜 사이 조회 (날짜 범위 밖일 경우)")
    void findNoneActivityInfoBetweenDate() {
        // given
        AdminDTO.ActivityInfoReqDTO activityInfoReqDTO = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword(null)
                .activityDivision(2)
                .from("2022/05/02")
                .to("2022/05/03")
                .build();

        // when
        List<AdminDTO.ActivityInfoResDTO> activityInfo = adminQueryRepository.findActivityInfo(activityInfoReqDTO);

        // then
        assertThat(activityInfo.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("활동 정보 조회 - 활동 구분이 0인 경우")
    void findActivityInfoByActivityDivision0() {
        // given
        AdminDTO.ActivityInfoReqDTO activityInfoReqDTO = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword(null)
                .activityDivision(0)
                .from("2021/05/02")
                .to("2023/05/03")
                .build();

        // when
        List<AdminDTO.ActivityInfoResDTO> activityInfo = adminQueryRepository.findActivityInfo(activityInfoReqDTO);

        // then
        assertThat(activityInfo.size()).isEqualTo(1);
        assertThat(activityInfo.get(0).getStdId()).isEqualTo(member1.getStdId());
        assertThat(activityInfo.get(0).getActivityDate()).isEqualTo(activity1.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(activityInfo.get(0).getTotalDistance()).isEqualTo(activity1.getDistance());
        assertThat(activityInfo.get(0).getStartTime()).isEqualTo(activity1.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getEndTime()).isEqualTo(activity1.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(activityInfo.get(0).getStdName()).isEqualTo(member1.getName());
    }

    @Test
    @DisplayName("활동 정보 조회 - 활동 구분이 1인 경우")
    void findActivityInfoByActivityDivision1() {
        // given
        AdminDTO.ActivityInfoReqDTO activityInfoReqDTO = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword(null)
                .activityDivision(1)
                .from("2021/05/02")
                .to("2023/05/03")
                .build();

        // when
        List<AdminDTO.ActivityInfoResDTO> activityInfo = adminQueryRepository.findActivityInfo(activityInfoReqDTO);

        // then
        assertThat(activityInfo.size()).isEqualTo(1);
        assertThat(activityInfo.get(0).getStdId()).isEqualTo(member2.getStdId());
        assertThat(activityInfo.get(0).getActivityDate()).isEqualTo(activity2.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(activityInfo.get(0).getTotalDistance()).isEqualTo(activity2.getDistance());
        assertThat(activityInfo.get(0).getStartTime()).isEqualTo(activity2.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getEndTime()).isEqualTo(activity2.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        assertThat(activityInfo.get(0).getPartnerName()).isEqualTo(partner2.getPartnerName());
        assertThat(activityInfo.get(0).getStdName()).isEqualTo(member2.getName());
    }

    @Test
    @DisplayName("활동 정보 조회 - 활동 구분이 2인 경우")
    void findActivityInfoByActivityDivision2() {
        // given
        AdminDTO.ActivityInfoReqDTO activityInfoReqDTO = AdminDTO.ActivityInfoReqDTO.builder()
                .keyword(null)
                .activityDivision(2)
                .from("2022/05/01")
                .to("2022/12/31")
                .build();

        // when
        List<AdminDTO.ActivityInfoResDTO> activityInfo = adminQueryRepository.findActivityInfo(activityInfoReqDTO);

        // then
        assertThat(activityInfo.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("활동 세부 정보 조회")
    void findActivityDetailInfo() {
        // given
        Long activityId = activity1.getActivityId();

        // when
        AdminDTO.ActivityDetailInfoResDTO activityDetailInfo = adminQueryRepository.findActivityDetailInfo(activityId);

        // then
        assertThat(activityDetailInfo.getStdId()).isEqualTo(member1.getStdId());
        assertThat(activityDetailInfo.getStdName()).isEqualTo(member1.getName());
        assertThat(activityDetailInfo.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(activityDetailInfo.getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(activityDetailInfo.getReview()).isEqualTo(activity1.getReview());
        assertThat(activityDetailInfo.getActivityDate()).isEqualTo(activity1.getActivityDate());
        assertThat(activityDetailInfo.getStartTime()).isEqualTo(activity1.getStartTime());
        assertThat(activityDetailInfo.getEndTime()).isEqualTo(activity1.getEndTime());
        assertThat(activityDetailInfo.getTotalDistance()).isEqualTo(activity1.getDistance());
    }

    @Test
    @DisplayName("파트너 정보 조회")
    void findPartnerInfo() {
        // given
        String keyword = null;
        String partnerDetail = partner1.getPartnerDetail();

        // when
        List<AdminDTO.PartnerInfoResDTO> partnerInfo = adminQueryRepository.findPartnerInfo(keyword, partnerDetail);

        // then
        assertThat(partnerInfo.size()).isEqualTo(1);
        assertThat(partnerInfo.get(0).getStdId()).isEqualTo(member1.getStdId());
        assertThat(partnerInfo.get(0).getStdName()).isEqualTo(member1.getName());
        assertThat(partnerInfo.get(0).getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(partnerInfo.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(partnerInfo.get(0).getPartnerBirth()).isEqualTo(partner1.getPartnerBirth());
        assertThat(partnerInfo.get(0).getGender()).isEqualTo(partner1.getGender());
        assertThat(partnerInfo.get(0).getRelation()).isEqualTo(partner1.getRelationship());
        assertThat(partnerInfo.get(0).getPartnerDivision()).isEqualTo(partner1.getPartnerDivision());

    }

}