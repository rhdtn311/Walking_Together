package backend.server.service.feed;

import backend.server.entity.Activity;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.exception.feedService.ActiveActivityNotWriteReviewException;
import backend.server.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FeedReviewService {

    private final ActivityRepository activityRepository;

    @Transactional
    public Long writeActivityReview(Long activityId, String review) {
        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if(activityOptional.isEmpty()) {
            throw new ActivityNotFoundException();
        }

        Activity activity = activityOptional.get();
        if (activity.getActivityStatus() == 1) {
            throw new ActiveActivityNotWriteReviewException();
        }

        activity.changeReview(review);

        return activityId;
    }
}
