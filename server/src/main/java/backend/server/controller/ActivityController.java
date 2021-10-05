package backend.server.controller;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.activity.PartnerDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ErrorCode;
import backend.server.exception.activityService.*;
import backend.server.service.activity.ActivityCreationService;
import backend.server.service.activity.ActivityDeleteService;
import backend.server.service.activity.ActivityEndService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class ActivityController {

    private final ActivityCreationService activityCreationService;
    private final ActivityEndService activityEndService;
    private final ActivityDeleteService activityDeleteService;

    // 활동 생성 화면
    @GetMapping("/activity/create")
    public ResponseEntity<ResponseDTO> createActivity(@RequestParam(value = "stdId") String stdId) {

        List<PartnerDTO.PartnerListRes> partnerListRes = activityCreationService.createActivity(stdId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너 리스트 불러오기 완료")
                .data(partnerListRes)
                .build());
    }

    // 활동 생성 완료
    @PostMapping("/activity/createActivity")
    public ResponseEntity<ResponseDTO> createActivityDone(@ModelAttribute ActivityDTO.ActivityCreationReq activityCreationReq) {

        Long activityId = activityCreationService.createActivityDone(activityCreationReq);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("활동 생성 완료")
                .data(activityId)
                .build());
    }

    // MediaType.MULTIPART_FORM_DATA_VALUE
    // 활동 종료
    @PostMapping(value = "/activity/end")
    public ResponseEntity<ResponseDTO> endActivity(@ModelAttribute ActivityDTO.ActivityEndReq activityEndReq) {

        Long result = activityEndService.endActivity(activityEndReq);

        if (result.equals(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME.getCode()))
            throw new ActivityAbnormalDoneWithoutMinimumTimeException();
        else if (result.equals(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode()))
            throw new ActivityAbnormalDoneWithoutMinimumDistanceException();
        else if (result.equals(ErrorCode.MINIMUM_ACTIVITY_TIME_NOT_SATISFY.getCode()))
            throw new MinimumActivityTimeNotSatisfyException();
        else if (result.equals(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode())) {
            throw new MinimumActivityDistanceNotSatisfyException();
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("저장이 완료 되었습니다.")
                .data(result)
                .build());
    }

    // 활동 삭제
    @PostMapping("/activity/delete")
    public ResponseEntity<ResponseDTO> deleteActivity(@RequestParam(value = "activityId") Long activityId) {

        Long result = activityDeleteService.deleteActivity(activityId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("성공적으로 삭제 되었습니다.")
                .data(result)
                .build());
    }

    // 활동 비정상 종료시 학번 리턴
    @PostMapping("/returnId")
    public String tokenToStdId(@RequestBody TokenDTO tokenDTO) {
        return activityEndService.tokenToStdId(tokenDTO);
    }

}