package backend.server.controller;

import backend.server.DTO.admin.*;
import backend.server.DTO.response.ResponseDTO;
import backend.server.service.admin.ActivityInfoService;
import backend.server.service.admin.MemberInfoService;
import backend.server.service.admin.PartnerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final MemberInfoService memberInfoService;
    private final ActivityInfoService activityInfoService;
    private final PartnerInfoService partnerInfoService;

    // 학생정보조회
    @GetMapping("/admin/userinfo")
    public ResponseEntity<ResponseDTO> getMemberInfo(@RequestParam(value = "keyword") @Nullable String keyword) {

        List<AdminDTO.MemberInfoResDTO> memberInfo = memberInfoService.getMemberInfo(keyword);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(memberInfo)
                .build());
    }

    // 활동정보조회
    @GetMapping("/admin/activityInfo")
    public ResponseEntity<ResponseDTO> getActivityInfo(AdminDTO.ActivityInfoReqDTO activityInfoReqDTO) {

        List<AdminDTO.ActivityInfoResDTO> activityInfo = activityInfoService.getActivityInfo(activityInfoReqDTO);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(activityInfo)
                .build());
    }

    // 특정 활동 상세 조회
    @GetMapping("/admin/activityInfo/detail")
    public ResponseEntity<ResponseDTO> getActivityInfoDetail(Long activityId) {

        AdminDTO.ActivityDetailInfoResDTO activityDetailInfo = activityInfoService.getActivityDetailInfo(activityId);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(activityDetailInfo)
                .build());
    }

    // 파트너 정보 조회
    @GetMapping("/admin/partnerInfo")
    public ResponseEntity<ResponseDTO> getPartnerInfo(@RequestParam(value = "keyword") @Nullable String keyword,
                                               @RequestParam(value = "partnerDetail") @Nullable String partnerDetail) {

        List<AdminDTO.PartnerInfoResDTO> partners = partnerInfoService.getPartnerInfo(keyword, partnerDetail);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(partners)
                .build());
    }
}
