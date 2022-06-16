package backend.server.service.activity;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Activity;
import backend.server.entity.ActivityCheckImages;
import backend.server.entity.Member;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.s3.FileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityDeleteService {
    private final ActivityRepository activityRepository;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;

    private final CertificationDeleteService certificationDeleteService;

    private final FileDeleteService fileDeleteService;

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

        List<ActivityCheckImages> activityCheckImages = activity.getActivityCheckImages();
        if (activityCheckImages != null) {
            activityCheckImages.forEach(image -> fileDeleteService.deleteFile(activityCheckImagesRepository, new FileDeleteDTO(activityId)));
        }
        certificationDeleteService.deleteCertification(activityId);
        activityRepository.delete(activity);

        return activityId;
    }
}
