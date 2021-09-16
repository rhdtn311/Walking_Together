package backend.server.service.activity;

import backend.server.DTO.ActivityDTO;
import backend.server.DTO.TokenDTO;
import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.exception.activityService.ActivityAlreadyDoneException;
import backend.server.exception.activityService.ActivityDonePhotoNotSendException;
import backend.server.exception.activityService.ActivityMapPhotoNotSendException;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.s3.FileUploadService;
import backend.server.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityEndService {
    private final FileUploadService fileUploadService;
    private final MapCaptureSaveService mapCaptureSaveService;
    private final CertificationSaveService certificationSaveService;
    private final ActivityRepository activityRepository;

    private final TokenProvider tokenProvider;

    // 활동 종료
    @Transactional
    public Long endActivity(ActivityDTO.ActivityEndReq activityEndReq) {

        Optional<Activity> activityOptional = activityRepository.findById(activityEndReq.getActivityId());

        if (activityOptional.isEmpty()) {
            throw new ActivityNotFoundException();
        }

        Activity activity = activityOptional.get();
        if (activity.getEndTime() != null) {
            throw new ActivityAlreadyDoneException();
        }

        long checkActivityResult = activity.checkAndSaveActivity(activityEndReq.getDistance(),activityEndReq.getCheckNormalQuit(),activityEndReq.getActivityEndTime());
        if (checkActivityResult != 0) {
            return checkActivityResult;
        }

        if (activityEndReq.getCheckNormalQuit() == 0) {
            if (activityEndReq.getEndPhoto() != null) {
                fileUploadService.uploadMapImages(activityEndReq.getEndPhoto(),activityEndReq.getActivityId(), "end");
            } else {
                throw new ActivityDonePhotoNotSendException();
            }
        }

        if (activityEndReq.getMapToArray() == null) {
            throw new ActivityMapPhotoNotSendException();
        }

        // 맵 경로 저장
        mapCaptureSaveService.saveMapCapture(activityEndReq.mapArrayToHashMap(), activity.getActivityId());

        // 활동 저장
        activity.changeDistance(activityEndReq.getDistance());
        activity.changeEndTime(activityEndReq.getActivityEndTime());
        activity.changeActivityStatus(0);
        // 활동 시간 변경
        activity.changeTotalTime();

        // 활동한 회원의 총 거리 변경
        Member member = activity.getMember();
        member.addDistance(activityEndReq.getDistance());
        member.changeTotalTime(activity, "plus"); // 회원의 총 환산 시간 변경

        certificationSaveService.saveCertification(member, activity);

        return activityEndReq.getActivityId();
    }

    public String tokenToStdId(TokenDTO tokenDTO) {

        Authentication authentication = tokenProvider.getAuthentication(tokenDTO.getToken());
        return authentication.getName();
    }
}
