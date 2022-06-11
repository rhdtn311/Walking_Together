package backend.server.service.activity;

import backend.server.entity.ActivityCheckImages;
import backend.server.repository.ActivityCheckImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityCheckImagesDeleteService {
    private final ActivityCheckImagesRepository activityCheckImagesRepository;

    @Transactional
    public long deleteActivityCheckImages(Long activityId) {
        Optional<List<ActivityCheckImages>> activityCheckImagesByActivityId
                = activityCheckImagesRepository.findImagesByActivityId(activityId);
        if (activityCheckImagesByActivityId.isPresent()) {
            List<ActivityCheckImages> activityCheckImages = activityCheckImagesByActivityId.get();
            for (Object image : activityCheckImages) {
                activityCheckImagesRepository.delete((ActivityCheckImages) image);
            }
        }
        return activityId;
    }
}
