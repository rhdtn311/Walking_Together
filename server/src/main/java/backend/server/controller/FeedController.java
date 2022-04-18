package backend.server.controller;

import backend.server.DTO.feed.FeedDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.feedService.ReviewNotReceiveException;
import backend.server.service.feed.FeedCertificationService;
import backend.server.service.feed.FeedDetailService;
import backend.server.service.feed.FeedMainService;
import backend.server.service.feed.FeedReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FeedController {

    private final FeedMainService feedMainService;
    private final FeedDetailService feedDetailService;
    private final FeedReviewService feedReviewService;
    private final FeedCertificationService feedCertificationService;

    // 피드 메인
    @GetMapping("/feed")
    public ResponseEntity<ResponseDTO> getFeedMain(@RequestParam(value = "stdId") String stdId,
                                                   @RequestParam(value = "sort") String sort) {
        List<FeedDTO.FeedMainResDTO> feedMainDTO = feedMainService.getFeedMain(stdId, sort);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(feedMainDTO)
                .build());
    }

    // 피드 상세
    @GetMapping("/feed/detail")
    public ResponseEntity<ResponseDTO> getFeedDetail(@RequestParam("activityId") Long activityId) {
        FeedDTO.FeedDetailResDTO feedDetail = feedDetailService.getFeedDetail(activityId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(feedDetail)
                .build());
    }

    // 소감문
    @PostMapping("/feed/detail/review")
    public ResponseEntity<ResponseDTO> writeActivityReview(@RequestParam(value="activityId") Long activityId,
                                          @RequestParam(value = "review") @Nullable String review) {
        if (review == null) {
            throw new ReviewNotReceiveException();
        }

        Long reviewActivityId = feedReviewService.writeActivityReview(activityId, review);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("소감문 저장 완료")
                .data(reviewActivityId)
                .build());
    }

    // 인증서
    @PostMapping("/feed/certification")
    public ResponseEntity<ResponseDTO> createCertification(String stdId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate fromDate = LocalDate.parse(from, formatter);
        LocalDate toDate = LocalDate.parse(to, formatter);

        FeedDTO.CertificationResDTO certification = feedCertificationService.findCertification(fromDate, toDate, stdId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("발급 완료")
                .data(certification)
                .build());
    }
}
