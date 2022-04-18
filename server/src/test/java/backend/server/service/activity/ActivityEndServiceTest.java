package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.user.UserDTO;
import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.activityService.ActivityAlreadyDoneException;
import backend.server.exception.activityService.ActivityDonePhotoNotSendException;
import backend.server.exception.activityService.ActivityMapPhotoNotSendException;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ActivityEndServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityEndService activityEndService;

    @Test
    @DisplayName("활동이 정상 종료일 때 확인 : 일반 활동")
    void activityNormalDoneWithNormalActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        // 활동 전 회원의 총 활동 거리
        long beforeActivityDistance = member.getDistance();
        int beforeActivityTotalTime = member.getTotalTime();

        Partner partner = Partner.builder()
                .partnerName("OrdinaryPartner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // 일반 활동
        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(0)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityEndReqDTO activityEndReq =
                new ActivityDTO.ActivityEndReqDTO(
                          savedActivity.getActivityId()
                        , 4300L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // when
        Long endActivityId = activityEndService.endActivity(activityEndReq); // 활동 종료

        Activity endActivity = activityRepository.findById(endActivityId).get();

        // then
        // 시작 활동의 객체와 활동이 종료됐을 때의 객체가 같은지 확인
        assertThat(startActivity).isSameAs(endActivity);
        // 활동이 종료 됐으므로 activityStatus가 0으로 바뀌었는지 확인
        assertThat(endActivity.getActivityStatus()).isEqualTo(0);
        // 활동 거리 확인
        assertThat(endActivity.getDistance()).isEqualTo(4300L);
        // 활동 종료 시간 확인
        assertThat(endActivity.getEndTime()).isEqualTo(LocalDateTime.of(2021, 1, 1, 1, 40, 00));
        // 회원의 총 거리 변경 확인 (회원의 총 거리 + 이번 활동 거리)
        assertThat(member.getDistance()).isEqualTo(beforeActivityDistance + startActivity.getDistance());
        // 회원의 총 시간 변경 확인 (1시간 30분) -> 로직은 ActivityTest에서 테스트
        assertThat(member.getTotalTime()).isEqualTo(beforeActivityTotalTime + 90);
    }

    @Test
    @DisplayName("활동이 정상 종료일 때 확인 : 돌봄 활동")
    void activityNormalDoneWithCareActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        // 활동 전 회원의 총 활동 거리
        long beforeActivityDistance = member.getDistance();
        int beforeActivityTotalTime = member.getTotalTime();

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // 돌봄 활동
        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityEndReqDTO activityEndReqDTO =
                new ActivityDTO.ActivityEndReqDTO(
                        savedActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // when
        activityEndService.endActivity(activityEndReqDTO);

        Activity endActivity = activityRepository.findById(startActivity.getActivityId()).get();

        // then
        // 활동이 시작했을 때와 끝났을 때 활동이 같은 객체인지 확인
        assertThat(startActivity).isSameAs(endActivity);
        // 활동이 종료 됐으므로 activityStatus가 0으로 바뀌었는지 확인
        assertThat(startActivity.getActivityStatus()).isEqualTo(0);
        // 활동 거리 확인
        assertThat(endActivity.getDistance()).isEqualTo(2500L);
        // 활동 종료 시간 확인
        assertThat(endActivity.getEndTime()).isEqualTo(LocalDateTime.of(2021, 1, 1, 1, 40, 00));
        // 회원의 총 거리 변경 확인 (회원의 총 거리 + 이번 활동 거리)
        assertThat(member.getDistance()).isEqualTo(beforeActivityDistance + startActivity.getDistance());
        // 회원의 총 시간 변경 확인 (1시간 30분) -> 로직은 ActivityTest에서 테스트
        assertThat(member.getTotalTime()).isEqualTo(beforeActivityTotalTime + 90);
    }

    @Test
    @DisplayName("비즈니스 예외 : 존재하지 않는 활동일 때 ActivityNotFoundException 처리 확인")
    void activityNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        // 존재하지 않는 activityId로 activityEndReqDTO 생성
        ActivityDTO.ActivityEndReqDTO notExistActivityEndReq =
                new ActivityDTO.ActivityEndReqDTO(
                        0L
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // 존재하는 activityId로 activityEndReqDTO 생성
        ActivityDTO.ActivityEndReqDTO existActivityEndReq =
                new ActivityDTO.ActivityEndReqDTO(
                        savedActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // then
        assertThrows(ActivityNotFoundException.class, () -> activityEndService.endActivity(notExistActivityEndReq));
        assertDoesNotThrow(() -> activityEndService.endActivity(existActivityEndReq));
    }

    @Test
    @DisplayName("비즈니스 예외 : 이미 종료된 활동일 때 ActivityAlreadyDoneException 처리 확인")
    void alreadyDoneActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityEndReqDTO activityEndReq =
                new ActivityDTO.ActivityEndReqDTO(
                        savedActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // when
        activityEndService.endActivity(activityEndReq);

        // then
        assertThrows(ActivityAlreadyDoneException.class, () -> activityEndService.endActivity(activityEndReq));

    }

    @Test
    @DisplayName("활동 시간이 30분 미만 이고 정상 종료일 때 해당 활동의 상태가 0, 활동 종료 시간이 정상적으로 처리, 일반 활동 시간이 0시간 0분, 돌봄 활동 시간이 0시간 0분, 활동 거리가 0으로 저장되었는지 확인")
    void activityDoneUnder30MinutesWithNormalQuitSaveNormally() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        // 거리는 만족했으나, 활동 시간은 29분으로 만족하지 않았고, 정상 종료인 경우
        ActivityDTO.ActivityEndReqDTO notSatisfyMinimumActivityTimeDTO =
                new ActivityDTO.ActivityEndReqDTO(
                        savedActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101002900"
                        , 0);

        activityEndService.endActivity(notSatisfyMinimumActivityTimeDTO);

        Activity notSatisfyTimeActivity = activityRepository.findById(startActivity.getActivityId()).get();

        // then
        assertThat(notSatisfyTimeActivity).isSameAs(savedActivity);
        // 활동 종료가 됐기 때문에 activityStatus가 0인지 확인
        assertThat(notSatisfyTimeActivity.getActivityStatus()).isEqualTo(0);
        // 활동 종료 시간 확인
        assertThat(notSatisfyTimeActivity.getEndTime()).isEqualTo(savedActivity.getEndTime());
        // 일반 활동 시간이 0시 0분으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getOrdinaryTime()).isEqualTo(LocalTime.of(0, 0));
        // 돌봄 활동 시간이 0시 0분으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getCareTime()).isEqualTo(LocalTime.of(0, 0));
        // 활동 거리가 0으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getDistance()).isEqualTo(0L);
    }

    @Test
    @DisplayName("활동 시간이 30분 미만이고 비정상 종료일 때 해당 활동의 상태가 0, 활동 종료 시간이 정상적으로 처리, 일반 활동 시간이 0시간 0분, 돌봄 활동 시간이 0시간 0분, 활동 거리가 0으로 저장되었는지 확인")
    void activityDoneUnder30MinutesWithAbNormalQuitSaveNormally() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        // 거리는 만족했으나, 활동 시간은 29분으로 만족하지 않았고, 비정상 종료인 경우
        ActivityDTO.ActivityEndReqDTO notSatisfyMinimumActivityTimeDTO =
                new ActivityDTO.ActivityEndReqDTO(
                        savedActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101002900"
                        , 1);

        activityEndService.endActivity(notSatisfyMinimumActivityTimeDTO);

        Activity notSatisfyTimeActivity = activityRepository.findById(startActivity.getActivityId()).get();

        // then
        assertThat(notSatisfyTimeActivity).isSameAs(savedActivity);
        // 활동 종료가 됐기 때문에 activityStatus가 0인지 확인
        assertThat(notSatisfyTimeActivity.getActivityStatus()).isEqualTo(0);
        // 활동 종료 시간 확인
        assertThat(notSatisfyTimeActivity.getEndTime()).isEqualTo(savedActivity.getEndTime());
        // 일반 활동 시간이 0시 0분으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getOrdinaryTime()).isEqualTo(LocalTime.of(0, 0));
        // 돌봄 활동 시간이 0시 0분으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getCareTime()).isEqualTo(LocalTime.of(0, 0));
        // 활동 거리가 0으로 저장 되었는지 확인
        assertThat(notSatisfyTimeActivity.getDistance()).isEqualTo(0L);
    }

    @Test
    @DisplayName("맵 경로 사진이 전송되지 않았을 때 ActivityMapPhotoNotSendException 처리 확인")
    void notSendMapCapture() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        // 활동이 정상 종료 되었는데 활동 경로만 보내지지 않았을 경우
        ActivityDTO.ActivityEndReqDTO notSendMapCaptureDTO =
                new ActivityDTO.ActivityEndReqDTO(
                        startActivity.getActivityId()
                        , 2500L
                        , null
                        , mockMultipartFile
                        , "20210101003000"
                        , 0);

        // then
        assertThrows(ActivityMapPhotoNotSendException.class,
                () -> activityEndService.endActivity(notSendMapCaptureDTO));
    }

    @Test
    @DisplayName("활동이 정상 종료 되었는데  활동 종료 사진이 전송되지 않았을 때 ActivityDonePhotoNotSendException 처리 확인")
    void activityEndPhotoNotSend() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity startActivity = Activity.builder()
                .member(member)
                .partner(partner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        Activity savedActivity = activityRepository.save(startActivity);

        // when
        // 활동이 정상 종료 되었는데 활동 경로만 보내지지 않았을 경우
        ActivityDTO.ActivityEndReqDTO notSendActivityEndPhoto =
                new ActivityDTO.ActivityEndReqDTO(
                        startActivity.getActivityId()
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , null
                        , "20210101003000"
                        , 0);

        // then
        assertThrows(ActivityDonePhotoNotSendException.class, () -> activityEndService.endActivity(notSendActivityEndPhoto));
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

}