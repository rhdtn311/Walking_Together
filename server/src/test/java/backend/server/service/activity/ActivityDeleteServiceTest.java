package backend.server.service.activity;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.*;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.*;
import backend.server.repository.s3.FileDeleteRepository;
import backend.server.s3.FileDeleteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class ActivityDeleteServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityDeleteService activityDeleteService;

    @Autowired
    ActivityCheckImagesRepository activityCheckImagesRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    MapCaptureRepository mapCaptureRepository;

    @MockBean
    FileDeleteService fileDeleteService;

    @Test
    @DisplayName("활동이 종료된 활동인 경우 : 회원의 총 거리에서 해당 활동 거리가 제외되는지 확인")
    void memberTotalDistanceMinusWhenActivityDelete() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .distance(100L)
                .name("member")
                .password("password")
                .email("email@naver.com")
                .build();

        Long originalMemberDistance = member.getDistance();
        memberRepository.save(member);

        Activity activity = Activity.builder()
                .member(member)
                .distance(60L)
                .activityDivision(0)
                .ordinaryTime(LocalTime.now())
                .build();

        activityRepository.save(activity);

        // when
        activityDeleteService.deleteActivity(activity.getActivityId());

        // then
        System.out.println("memberDistance : " + member.getDistance());
        assertThat(member.getDistance()).isEqualTo(originalMemberDistance - activity.getDistance());
    }

    @Test
    @DisplayName("활동이 종료된 활동인 경우 : 회원의 총 시간에서 해당 활동의 시간이 제외되는지 확인 ")
    void memberTotalTimeMinusWhenActivityDelete() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .distance(100L)
                .totalTime(600)
                .name("member")
                .password("password")
                .email("email@naver.com")
                .build();

        int originalMemberTotalTime = member.getTotalTime();
        memberRepository.save(member);

        Activity activity = Activity.builder()
                .member(member)
                .distance(60L)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(1, 30))   // 90분
                .build();

        activityRepository.save(activity);

        // when
        activityDeleteService.deleteActivity(activity.getActivityId());

        // then
        assertThat(member.getTotalTime()).isEqualTo(originalMemberTotalTime - 90);
    }

    @Test
    @DisplayName("활동 사진이 삭제되는지 확인")
    void activityCheckImageDelete() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .distance(100L)
                .totalTime(600)
                .name("member")
                .password("password")
                .email("email@naver.com")
                .build();
        memberRepository.save(member);

        Activity activity = Activity.builder()
                .member(member)
                .distance(60L)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(1, 30))
                .activityCheckImages(new ArrayList<>())
                .build();
        activityRepository.save(activity);

        ActivityCheckImages activityStartCheckImage = ActivityCheckImages.builder()
//                .activityId(activity.getActivityId())
                .activity(activity)
                .imageName("startActivityName")
                .imageUrl("startActivityURL")
                .build();

        ActivityCheckImages activityEndCheckImage = ActivityCheckImages.builder()
//                .activityId(activity.getActivityId())
                .activity(activity)
                .imageName("endActivityName")
                .imageUrl("endActivityURL")
                .build();

        activityCheckImagesRepository.save(activityStartCheckImage);
        activityCheckImagesRepository.save(activityEndCheckImage);

        activity.getActivityCheckImages().add(activityStartCheckImage);
        activity.getActivityCheckImages().add(activityEndCheckImage);

        doNothing().when(fileDeleteService).deleteFile(any(FileDeleteRepository.class), any(FileDeleteDTO.class));

        // when
        activityDeleteService.deleteActivity(activity.getActivityId());

        // then
        assertThat(activityCheckImagesRepository.findImagesByActivityId(activity.getActivityId()).get()).isEmpty();
    }

    @Test
    @DisplayName("활동의 인증서가 삭제되는지 확인")
    void activityCertificationDelete() {
        // given
        Member member = Member.builder()
                .stdId("stdId")
                .distance(100L)
                .totalTime(600)
                .name("member")
                .password("password")
                .email("email@naver.com")
                .build();

        memberRepository.save(member);

        Activity activity = Activity.builder()
                .member(member)
                .distance(60L)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(1, 30))
                .build();

        activityRepository.save(activity);

        Certification certification = Certification.builder()
                .certificationId(activity.getActivityId())
                .activityId(activity.getActivityId())
                .ordinaryTime(activity.getOrdinaryTime())
                .stdId(member.getStdId())
                .build();

        certificationRepository.save(certification);

        // when
        activityDeleteService.deleteActivity(activity.getActivityId());

        // then
        assertThat(certificationRepository.findById(activity.getActivityId())).isEmpty();
    }

    @Test
    @DisplayName("활동 경로가 삭제되는지 확인")
    void activityMapCaptureDelete() {
        // given
        Member member = Member.builder()
                .stdId("stdId")
                .distance(100L)
                .totalTime(600)
                .name("member")
                .password("password")
                .email("email@naver.com")
                .build();

        memberRepository.save(member);

        Activity activity = Activity.builder()
                .member(member)
                .distance(60L)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(1, 30))
                .build();

        activityRepository.save(activity);

        MapCapture mapCapture1 = MapCapture.builder()
                .lon("lon1")
                .lat("lat1")
                .timestamp("timestamp1")
                .activityId(activity.getActivityId())
                .build();

        MapCapture mapCapture2 = MapCapture.builder()
                .lon("lon2")
                .lat("lat2")
                .timestamp("timestamp2")
                .activityId(activity.getActivityId())
                .build();

        MapCapture mapCapture3 = MapCapture.builder()
                .lon("lon3")
                .lat("lat3")
                .timestamp("timestamp3")
                .activityId(activity.getActivityId())
                .build();

        mapCaptureRepository.save(mapCapture1);
        mapCaptureRepository.save(mapCapture2);
        mapCaptureRepository.save(mapCapture3);

        // when
        activityDeleteService.deleteActivity(activity.getActivityId());

        // then
        assertThat(mapCaptureRepository.findAllByActivityId(activity.getActivityId())).isEmpty();
    }

    @Test
    @DisplayName("비즈니스 예외 : 해당 활동이 존재하지 않는 경우 ActivityNotFoundException 처리 확인")
    void activityNotExist() {

        long notExistActivityId = 1L;

        // then
        assertThrows(ActivityNotFoundException.class, () -> activityDeleteService.deleteActivity(notExistActivityId));
    }
}