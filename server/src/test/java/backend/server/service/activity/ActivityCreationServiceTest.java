package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.activity.PartnerDTO;
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
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
@SpringBootTest
class ActivityCreationServiceTest {

    @Autowired
    private ActivityCreationService activityCreationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityCheckImagesRepository activityCheckImagesRepository;

    @Test
    @DisplayName("파트너 리스트가 제대로 출력되는지 확인")
    void partnerList() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner1 = Partner.builder()
                .partnerId(1L)
                .partnerName("partner1")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reaso1")
                .gender("여성")
                .member(member)
                .build();

        Partner partner2 = Partner.builder()
                .partnerId(2L)
                .partnerName("partner2")
                .partnerDivision(1)
                .partnerDetail("c")
                .partnerBirth("2004-01-03")
                .relationship("동생")
                .selectionReason("reason2")
                .gender("남성")
                .member(member)
                .build();

        partnerRepository.save(partner1);
        partnerRepository.save(partner2);

        // when
        List<PartnerDTO.PartnerListRes> partnerList
                = activityCreationService.createActivity(member.getStdId());

        // then
        assertThat(partnerList.size()).isEqualTo(2);
        assertThat(partnerList.get(0).getPartnerName()).isEqualTo(partner1.getPartnerName());
        assertThat(partnerList.get(1).getPartnerName()).isEqualTo(partner2.getPartnerName());
    }

    @Test
    @DisplayName("해당 회원이 활동 중일 때 비즈니스 예외 처리 확인")
    void memberAlreadyActive() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reaso1")
                .gender("여성")
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
    @DisplayName("해당 회원의 파트너가 존재하지 않을 때 ")
    void memberDoNotHavePartner() {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        // then
        assertThrows(PartnerNotFoundException.class, () -> activityCreationService.createActivity(member.getStdId()));
    }

    @Test
    @DisplayName("활동 생성 완료 시 활동 저장")
    void activityCreate() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reaso1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        // when
        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReq doneActivity = ActivityDTO.ActivityCreationReq.builder()
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
    @DisplayName("활동 생성 완료 시 활동 사진 저장")
    void activityCreateWithActivityStartImage() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reaso1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReq doneActivity = ActivityDTO.ActivityCreationReq.builder()
                .stdId(member.getStdId())
                .partnerId(partner.getPartnerId())
                .startPhoto(mockMultipartFile)
                .build();

        Long savedActivityId = activityCreationService.createActivityDone(doneActivity);

        // when
        ActivityCheckImages activityStartImage
                = activityCheckImagesRepository.findActivityCheckImagesByActivityId(savedActivityId).get().get(0);

        // then
        assertThat(activityStartImage.getActivityId()).isEqualTo(savedActivityId);
        System.out.println(activityStartImage.getImageName());
        assertTrue(activityStartImage.getImageName().contains("Activity_Start"));
    }

    @Test
    @DisplayName("비즈니스 예외 : 파트너가 존재하지 않을 때")
    void partnerNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        ActivityDTO.ActivityCreationReq doneActivity = ActivityDTO.ActivityCreationReq.builder()
                .stdId(member.getStdId())
                .partnerId(Long.MIN_VALUE)
                .startPhoto(mockMultipartFile)
                .build();

        // then
        assertThrows(PartnerNotFoundException.class,
                () -> activityCreationService.createActivityDone(doneActivity));
    }

    @Test
    @DisplayName("비즈니스 예외 : 존재하지 않는 회원일 때")
    void memberNotExist() throws IOException {
        // given
        Member member = Member.builder()
                .name("member1")
                .birth("19960311")
                .stdId("stdId1")
                .password("password")
                .department("학과1")
                .email("email1@naver.com")
                .phoneNumber("01011111111")
                .build();

        userRepository.save(member);

        Partner partner = Partner.builder()
                .partnerName("partner")
                .partnerDivision(0)
                .partnerDetail("o")
                .partnerBirth("1996-01-03")
                .relationship("지인")
                .selectionReason("reason1")
                .gender("여성")
                .member(member)
                .build();

        partnerRepository.save(partner);

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        ActivityDTO.ActivityCreationReq doneActivity = ActivityDTO.ActivityCreationReq.builder()
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