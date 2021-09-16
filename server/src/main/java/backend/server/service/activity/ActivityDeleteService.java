package backend.server.service.activity;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityDeleteService {
    private final ActivityRepository activityRepository;
    private final CertificationDeleteService certificationDeleteService;
    private final MapCaptureDeleteService mapCaptureDeleteService;
    private final ActivityCheckImagesDeleteService activityCheckImagesDeleteService;

    // 활동 삭제
    public Long deleteActivity(Long activityId) {

        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if(activityOptional.isEmpty()) {
            throw new ActivityNotFoundException();
        }
        Activity activity = activityOptional.get();
        Member member = activity.getMember();

        if (!activity.isActive()) {
            member.minusDistance(activity.getDistance());
            member.changeTotalTime(activity, "minus");
        }

        certificationDeleteService.deleteCertification(activityId);
        mapCaptureDeleteService.deleteMapCaptures(activityId);
        activityCheckImagesDeleteService.deleteActivityCheckImages(activityId);
        activityRepository.delete(activity);


        return activityId;
    }
}
