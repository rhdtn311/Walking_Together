package backend.server.service.feed;

import backend.server.DTO.common.MapCaptureDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.Activity;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.querydsl.FeedQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FeedDetailService {

    private final ActivityRepository activityRepository;
    private final FeedQueryRepository feedQueryRepository;

    public FeedDTO.FeedDetailResDTO getFeedDetail(Long activityId) {

        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityNotFoundException::new);

        FeedDTO.FeedDetailResDTO feedDetail = feedQueryRepository.findFeedDetail(activityId);

        feedDetail.setMapPicture(MapCaptureDTO.MapCaptureResDTO.toDTOList(activity.getMapCaptures()));

        return feedDetail;
    }
}
