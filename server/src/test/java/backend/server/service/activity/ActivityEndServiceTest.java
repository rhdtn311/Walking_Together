package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.s3.fileUpload.FileUploadDTO;
import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.activityService.ActivityAlreadyDoneException;
import backend.server.exception.activityService.ActivityDonePhotoNotSendException;
import backend.server.exception.activityService.ActivityMapPhotoNotSendException;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class ActivityEndServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityEndService activityEndService;

    @PersistenceContext
    private EntityManager em;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    @DisplayName("????????? ?????? ????????? ??? ?????? : ?????? ??????")
    void activityNormalDoneWithNormalActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        // ?????? ??? ????????? ??? ?????? ??????
        long beforeActivityDistance = member.getDistance();
        int beforeActivityTotalTime = member.getTotalTime();

        Partner partner = Partner.builder()
                .partnerName("OrdinaryPartner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // ?????? ??????
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
        em.clear();
        Long endActivityId = activityEndService.endActivity(activityEndReq); // ?????? ??????
        Member findMember = memberRepository.findMemberByStdId(member.getStdId()).get();

        Activity endActivity = activityRepository.findById(endActivityId).get();

        // then
        // ?????? ????????? ????????? ????????? ???????????? ?????? ????????? ????????? ??????
        assertThat(startActivity.getActivityId()).isSameAs(endActivity.getActivityId());
        // ????????? ?????? ???????????? activityStatus??? 0?????? ??????????????? ??????
        assertThat(endActivity.getActivityStatus()).isEqualTo(0);
        // ?????? ?????? ??????
        assertThat(endActivity.getDistance()).isEqualTo(4300L);
        // ?????? ?????? ?????? ??????
        assertThat(endActivity.getEndTime()).isEqualTo(LocalDateTime.of(2021, 1, 1, 1, 40, 00));
        // ????????? ??? ?????? ?????? ?????? (????????? ??? ?????? + ?????? ?????? ??????)
        assertThat(member.getDistance()).isEqualTo(beforeActivityDistance + startActivity.getDistance());
        // ????????? ??? ?????? ?????? ?????? (1?????? 30???) -> ????????? ActivityTest?????? ?????????
        assertThat(findMember.getTotalTime()).isEqualTo(beforeActivityTotalTime + 90);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ??? ?????? : ?????? ??????")
    void activityNormalDoneWithCareActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        // ?????? ??? ????????? ??? ?????? ??????
        long beforeActivityDistance = member.getDistance();
        int beforeActivityTotalTime = member.getTotalTime();

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // ?????? ??????
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

        doReturn("fileUrl").when(fileUploadService).uploadFileToS3(any(FileUploadDTO.class));

        // when
        em.clear();
        activityEndService.endActivity(activityEndReqDTO);
        Member findMember = memberRepository.findMemberByStdId(member.getStdId()).get();

        Activity endActivity = activityRepository.findById(startActivity.getActivityId()).get();

        // then
        // ????????? ???????????? ?????? ????????? ??? ????????? ?????? ???????????? ??????
        assertThat(startActivity.getActivityId()).isSameAs(endActivity.getActivityId());
        // ????????? ?????? ???????????? activityStatus??? 0?????? ??????????????? ??????
        assertThat(endActivity.getActivityStatus()).isEqualTo(0);
        // ?????? ?????? ??????
        assertThat(endActivity.getDistance()).isEqualTo(2500L);
        // ?????? ?????? ?????? ??????
        assertThat(endActivity.getEndTime()).isEqualTo(LocalDateTime.of(2021, 1, 1, 1, 40, 00));
        // ????????? ??? ?????? ?????? ?????? (????????? ??? ?????? + ?????? ?????? ??????)
        assertThat(member.getDistance()).isEqualTo(beforeActivityDistance + startActivity.getDistance());
        // ????????? ??? ?????? ?????? ?????? (1?????? 30???) -> ????????? ActivityTest?????? ?????????
        assertThat(findMember.getTotalTime()).isEqualTo(beforeActivityTotalTime + 90);
    }

    @Test
    @DisplayName("???????????? ?????? : ???????????? ?????? ????????? ??? ActivityNotFoundException ?????? ??????")
    void activityNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        // ???????????? ?????? activityId??? activityEndReqDTO ??????
        ActivityDTO.ActivityEndReqDTO notExistActivityEndReq =
                new ActivityDTO.ActivityEndReqDTO(
                        0L
                        , 2500L
                        , "{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}"
                        , mockMultipartFile
                        , "20210101014000"
                        , 0);

        // ???????????? activityId??? activityEndReqDTO ??????
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
        em.clear();
        assertDoesNotThrow(() -> activityEndService.endActivity(existActivityEndReq));
    }

    @Test
    @DisplayName("???????????? ?????? : ?????? ????????? ????????? ??? ActivityAlreadyDoneException ?????? ??????")
    void alreadyDoneActivity() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        em.clear();
        activityEndService.endActivity(activityEndReq);

        // then
        assertThrows(ActivityAlreadyDoneException.class, () -> activityEndService.endActivity(activityEndReq));

    }

    @Test
    @DisplayName("?????? ????????? 30??? ?????? ?????? ?????? ????????? ??? ?????? ????????? ????????? 0, ?????? ?????? ????????? ??????????????? ??????, ?????? ?????? ????????? 0?????? 0???, ?????? ?????? ????????? 0?????? 0???, ?????? ????????? 0?????? ?????????????????? ??????")
    void activityDoneUnder30MinutesWithNormalQuitSaveNormally() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        // ????????? ???????????????, ?????? ????????? 29????????? ???????????? ?????????, ?????? ????????? ??????
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
        // ?????? ????????? ?????? ????????? activityStatus??? 0?????? ??????
        assertThat(notSatisfyTimeActivity.getActivityStatus()).isEqualTo(0);
        // ?????? ?????? ?????? ??????
        assertThat(notSatisfyTimeActivity.getEndTime()).isEqualTo(savedActivity.getEndTime());
        // ?????? ?????? ????????? 0??? 0????????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getOrdinaryTime()).isEqualTo(LocalTime.of(0, 0));
        // ?????? ?????? ????????? 0??? 0????????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getCareTime()).isEqualTo(LocalTime.of(0, 0));
        // ?????? ????????? 0?????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getDistance()).isEqualTo(0L);
    }

    @Test
    @DisplayName("?????? ????????? 30??? ???????????? ????????? ????????? ??? ?????? ????????? ????????? 0, ?????? ?????? ????????? ??????????????? ??????, ?????? ?????? ????????? 0?????? 0???, ?????? ?????? ????????? 0?????? 0???, ?????? ????????? 0?????? ?????????????????? ??????")
    void activityDoneUnder30MinutesWithAbNormalQuitSaveNormally() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        // ????????? ???????????????, ?????? ????????? 29????????? ???????????? ?????????, ????????? ????????? ??????
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
        // ?????? ????????? ?????? ????????? activityStatus??? 0?????? ??????
        assertThat(notSatisfyTimeActivity.getActivityStatus()).isEqualTo(0);
        // ?????? ?????? ?????? ??????
        assertThat(notSatisfyTimeActivity.getEndTime()).isEqualTo(savedActivity.getEndTime());
        // ?????? ?????? ????????? 0??? 0????????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getOrdinaryTime()).isEqualTo(LocalTime.of(0, 0));
        // ?????? ?????? ????????? 0??? 0????????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getCareTime()).isEqualTo(LocalTime.of(0, 0));
        // ?????? ????????? 0?????? ?????? ???????????? ??????
        assertThat(notSatisfyTimeActivity.getDistance()).isEqualTo(0L);
    }

    @Test
    @DisplayName("??? ?????? ????????? ???????????? ????????? ??? ActivityMapPhotoNotSendException ?????? ??????")
    void notSendMapCapture() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        // ????????? ?????? ?????? ???????????? ?????? ????????? ???????????? ????????? ??????
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
    @DisplayName("????????? ?????? ?????? ????????????  ?????? ?????? ????????? ???????????? ????????? ??? ActivityDonePhotoNotSendException ?????? ??????")
    void activityEndPhotoNotSend() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .distance(0L)
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("carePartner")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
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
        // ????????? ?????? ?????? ???????????? ?????? ????????? ???????????? ????????? ??????
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