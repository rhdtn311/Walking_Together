package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.activity.PartnerDTO;
import backend.server.DTO.s3.fileUpload.FileUploadDTO;
import backend.server.entity.Activity;
import backend.server.entity.ActivityCheckImages;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.activityService.ActivityAlreadyInProgressException;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
@SpringBootTest
class ActivityCreationServiceTest {

    @Autowired
    private ActivityCreationService activityCreationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityCheckImagesRepository activityCheckImagesRepository;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    @DisplayName("????????? ???????????? ????????? ??????????????? ??????")
    void partnerList() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner1 = Partner.builder()
                .partnerId(1L)
                .partnerName("partner1")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reaso1")
                .gender("??????")
                .member(member)
                .build();

        Partner partner2 = Partner.builder()
                .partnerId(2L)
                .partnerName("partner2")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("2004-01-03")
                .relationship("??????")
                .selectionReason("reason2")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner1);
        partnerRepository.save(partner2);

        // when
        List<PartnerDTO.PartnerListResDTO> partnerList
                = activityCreationService.createActivity(member.getStdId());

        // then
        assertThat(partnerList.size()).isEqualTo(2);
        assertThat(partnerList.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(partnerList.get(1).getPartnerName()).isEqualTo(partner2.getPartnerName());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??? ???????????? ?????? ?????? ??????")
    void memberAlreadyActive() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reaso1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        Activity activity = Activity.builder()
                .member(member)
                .partner(partner)
                .activityStatus(1)
                .build();

        activityRepository.save(activity);

        // then
        assertThrows(ActivityAlreadyInProgressException.class,
                () -> activityCreationService.createActivity(member.getStdId()));

    }

    @Test
    @DisplayName("?????? ????????? ???????????? ???????????? ?????? ??? ")
    void memberDoNotHavePartner() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        // then
        assertThrows(PartnerNotFoundException.class, () -> activityCreationService.createActivity(member.getStdId()));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??? ?????? ??????")
    void activityCreate() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reaso1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // when
        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReqDTO doneActivity = ActivityDTO.ActivityCreationReqDTO.builder()
                .stdId(member.getStdId())
                .partnerId(partner.getPartnerId())
                .startPhoto(mockMultipartFile)
                .build();

        Long doneActivityId = activityCreationService.createActivityDone(doneActivity);

        // then
        Activity findActivity = activityRepository.findById(doneActivityId).get();

        assertThat(doneActivityId).isEqualTo(findActivity.getActivityId());
        assertThat(findActivity.getMember().getStdId()).isEqualTo(findActivity.getMember().getStdId());
        assertThat(findActivity.getPartner().getPartnerId()).isEqualTo(findActivity.getPartner().getPartnerId());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??? ?????? ?????? ??????")
    void activityCreateWithActivityStartImage() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reaso1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReqDTO doneActivity = ActivityDTO.ActivityCreationReqDTO.builder()
                .stdId(member.getStdId())
                .partnerId(partner.getPartnerId())
                .startPhoto(mockMultipartFile)
                .build();

        Long savedActivityId = activityCreationService.createActivityDone(doneActivity);

        doReturn("fileURL").when(fileUploadService).uploadFileToS3(any(FileUploadDTO.class));

        // when
        ActivityCheckImages activityStartImage
                = activityCheckImagesRepository.findImagesByActivityId(savedActivityId).get().get(0);

        // then
        assertTrue(activityStartImage.getImageName().contains("Activity_Start"));
    }

    @Test
    @DisplayName("???????????? ?????? : ???????????? ???????????? ?????? ???")
    void partnerNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        ActivityDTO.ActivityCreationReqDTO doneActivity = ActivityDTO.ActivityCreationReqDTO.builder()
                .stdId(member.getStdId())
                .partnerId(Long.MIN_VALUE)
                .startPhoto(mockMultipartFile)
                .build();

        // then
        assertThrows(PartnerNotFoundException.class,
                () -> activityCreationService.createActivityDone(doneActivity));
    }

    @Test
    @DisplayName("???????????? ?????? : ???????????? ?????? ????????? ???")
    void memberNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("??????1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        memberRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("??????")
                .selectionReason("reason1")
                .gender("??????")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReqDTO doneActivity = ActivityDTO.ActivityCreationReqDTO.builder()
                .stdId("doNotExist")
                .partnerId(partner.getPartnerId())
                .startPhoto(mockMultipartFile)
                .build();

        assertThrows(MemberNotFoundException.class,
                () -> activityCreationService.createActivityDone(doneActivity));
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

}