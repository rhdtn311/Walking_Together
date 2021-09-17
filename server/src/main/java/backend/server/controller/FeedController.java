package backend.server.controller;

import backend.server.DTO.feed.FeedDTO;
import backend.server.DTO.feed.FeedDetailDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.feed.FeedMainService;
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
    public ResponseEntity<Message> feedDetail(@RequestParam("activityId") Long activityId) {

        FeedDetailDTO data = feedService.feedDetail(activityId);
        if(data == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 활동입니다.", 404L);
        }

        Message resBody = new Message();
        resBody.setData(data);
        resBody.setMessage("조회 완료");

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 소감문
    @PostMapping("/feed/detail/review")
    public ResponseEntity<Message> feedReview(@RequestParam(value="activityId") Long activityId,
                                          @RequestParam(value = "review") @Nullable String review) {

        FeedDetailDTO dto = FeedDetailDTO.builder()
                .review(review).build();

        Long result = feedService.activityReview(activityId, dto);

        if(result == 404L) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 활동입니다.", 400L);
        }

        if(result ==405L) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "소감문을 작성해주세요.", 405L);
        }

        if(result ==406L) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "진행 중인 활동은 소감문 작성이 불가능합니다.", 406L);
        }

        Message resBody = new Message();
        resBody.setMessage("소감문 저장 완료");

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 인증서
    @PostMapping("/feed/certification")
    public Map<String, Object> createCertification(String stdId, String from, String to) {

        Map<String, Object> certification = feedService.getCertification(from, to, stdId);

        return certification;
    }
}
