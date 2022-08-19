package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.DTO.s3.fileUpload.ActivityEndImageFileUploadDTO;
import backend.server.entity.Activity;
import backend.server.entity.ActivityCheckImages;
import backend.server.entity.MapCapture;
import backend.server.entity.Member;
import backend.server.exception.activityService.ActivityAlreadyDoneException;
import backend.server.exception.activityService.ActivityDonePhotoNotSendException;
import backend.server.exception.activityService.ActivityMapPhotoNotSendException;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import backend.server.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityEndService {

    private final CertificationSaveService certificationSaveService;
    private final ActivityRepository activityRepository;
    private final FileUploadService fileUploadService;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    // 활동 종료
    @Transactional
    public Long endActivity(ActivityDTO.ActivityEndReqDTO activityEndReqDTO) {

        Activity activity = activityRepository.findById(activityEndReqDTO.getActivityId()).orElseThrow(ActivityNotFoundException::new);

        if (activity.getEndTime() != null) {
            throw new ActivityAlreadyDoneException();
        }

        long checkActivityResult = activity.checkAndSaveActivity(activityEndReqDTO.getDistance(),activityEndReqDTO.getCheckNormalQuit(),activityEndReqDTO.getActivityEndTime());
        if (checkActivityResult != 0) {
            return checkActivityResult;
        }

        if (activityEndReqDTO.getCheckNormalQuit() == 0) {
            if (activityEndReqDTO.getEndPhoto() != null) {
                ActivityEndImageFileUploadDTO activityEndImageFileUploadDTO = new ActivityEndImageFileUploadDTO(activityEndReqDTO.getEndPhoto());
                String fileUrl = fileUploadService.uploadFileToS3(activityEndImageFileUploadDTO);
                saveActivityEndImage(activity, fileUrl, activityEndImageFileUploadDTO.getFileName());
            } else {
                throw new ActivityDonePhotoNotSendException();
            }
        }

        if (activityEndReqDTO.getMapToArray() == null) {
            throw new ActivityMapPhotoNotSendException();
        }

        // 맵 경로 저장
        activity.setMapCaptures(activityEndReqDTO.toMapCaptures(activity));

        // 활동 저장
        activity.changeDistance(activityEndReqDTO.getDistance());
        activity.changeEndTime(activityEndReqDTO.getActivityEndTime());
        activity.changeActivityStatus(0);
        // 활동 시간 변경
        activity.changeTotalTime();

        // 활동한 회원의 총 거리 변경
        Member member = activity.getMember();
        member.addDistance(activityEndReqDTO.getDistance());
        member.changeTotalTime(activity, "plus"); // 회원의 총 환산 시간 변경

        certificationSaveService.saveCertification(member, activity);

        return activityEndReqDTO.getActivityId();
    }

    private void saveActivityEndImage(Activity activity, String fileUrl, String fileName) {
        ActivityCheckImages activityCheckImage = ActivityCheckImages.builder()
                .activity(activity)
                .imageUrl(fileUrl)
                .imageName(fileName)
                .build();

        activityCheckImagesRepository.save(activityCheckImage);
    }

    public String getStdIdFromToken(TokenDTO tokenDTO) {

        return jwtTokenProvider.getStdIdFromToken(tokenDTO.getToken());

    }
}
