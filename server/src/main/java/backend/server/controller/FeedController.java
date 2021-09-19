package backend.server.controller;

import backend.server.DTO.feed.FeedDTO;
import backend.server.DTO.feed.FeedDetailDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ApiException;
import backend.server.exception.feedService.ReviewNotReceiveException;
import backend.server.message.Message;
import backend.server.service.feed.FeedDetailService;
import backend.server.service.feed.FeedMainService;
import backend.server.service.feed.FeedReviewService;
import backend.server.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class FeedController {

    private final FeedService feedService;
    private final FeedMainService feedMainService;
    private final FeedDetailService feedDetailService;
    private final FeedReviewService feedReviewService;

    // 피드 메인
    @GetMapping("/feed")
    public ResponseEntity<ResponseDTO> getFeedMain(@RequestParam(value = "stdId") String stdId,
                                                   @RequestParam(value = "sort") String sort) {
        List<FeedDTO.FeedMainResDTO> feedMain = feedMainService.getFeedMain(stdId, sort);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(feedMain)
                .build());
    }

    // 피드 상세
    @GetMapping("/feed/detail")
    public ResponseEntity<ResponseDTO> feedDetail(@RequestParam("activityId") Long activityId) {
        FeedDTO.FeedDetailResDTO feedDetail = feedDetailService.getFeedDetail(activityId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(feedDetail)
                .build());
    }

    // 소감문
    @PostMapping("/feed/detail/review")
    public ResponseEntity<ResponseDTO> writeFeedReview(@RequestParam(value="activityId") Long activityId,
                                          @RequestParam(value = "review") @Nullable String review) {

        feedReviewService.writeActivityReview(activityId, review);
        if (review == null) {
            throw new ReviewNotReceiveException();
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("소감문 저장 완료")
                .data(activityId)
                .build());
    }














    // 인증서
    @PostMapping("/feed/certification")
    public Map<String, Object> createCertification(String stdId, String from, String to) {

        Map<String, Object> certification = feedService.getCertification(from, to, stdId);

        return certification;
    }
}
