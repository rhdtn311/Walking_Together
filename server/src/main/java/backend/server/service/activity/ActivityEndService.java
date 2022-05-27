package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.DTO.s3.fileUpload.ActivityEndImageFileUploadDTO;
import backend.server.entity.Activity;
import backend.server.entity.ActivityCheckImages;
import backend.server.entity.Member;
import backend.server.exception.activityService.ActivityAlreadyDoneException;
import backend.server.exception.activityService.ActivityDonePhotoNotSendException;
import backend.server.exception.activityService.ActivityMapPhotoNotSendException;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.s3.FileUploadService;
import backend.server.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityEndService {

    private final MapCaptureSaveService mapCaptureSaveService;
    private final CertificationSaveService certificationSaveService;
    private final ActivityRepository activityRepository;
    private final FileUploadService fileUploadService;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;

    private final JwtTokenProvider jwtTokenProvider;

    // 활동 종료
    @Transactional
    public Long endActivity(ActivityDTO.ActivityEndReqDTO activityEndReqDTO) {

        Optional<Activity> activityOptional = activityRepository.findById(activityEndReqDTO.getActivityId());

        if (activityOptional.isEmpty()) {
            throw new ActivityNotFoundException();
        }

        Activity activity = activityOptional.get();
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
                saveActivityEndImage(activity.getActivityId(), fileUrl, activityEndImageFileUploadDTO.getFileName());
            } else {
                throw new ActivityDonePhotoNotSendException();
            }
        }

        if (activityEndReqDTO.getMapToArray() == null) {
            throw new ActivityMapPhotoNotSendException();
        }

        // 맵 경로 저장
        mapCaptureSaveService.saveMapCapture(activityEndReqDTO.mapArrayToHashMap(), activity.getActivityId());

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

    private void saveActivityEndImage(Long activityId, String fileUrl, String fileName) {
        ActivityCheckImages activityCheckImage = ActivityCheckImages.builder()
                .activityId(activityId)
                .imageUrl(fileUrl)
                .imageName(fileName)
                .build();

        activityCheckImagesRepository.save(activityCheckImage);
    }

    public String getStdIdFromToken(TokenDTO tokenDTO) {

        return jwtTokenProvider.getStdIdFromToken(tokenDTO.getToken());

    }
}
