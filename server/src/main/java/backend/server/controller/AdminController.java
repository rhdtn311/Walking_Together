package backend.server.controller;

import backend.server.DTO.PartnerDTO;
import backend.server.DTO.admin.*;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.admin.AdminService;
import backend.server.service.admin.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final MemberInfoService memberInfoService;

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

        List<AdminDTO.ActivityInfoResDTO> activityInfoQueryResDTOS = adminService.getActivityInfo(activityInfoReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 완료")
                .data(activityInfoQueryResDTOS)
                .build());
    }

    // 특정 활동 상세 조회
    @GetMapping("/admin/activityInfo/detail")
    public ResponseEntity<Message> activityInfoDetail(Long activityId) {

        ActivityDetailInfoDTO result = adminService.activityDetail(activityId);

        if (result == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 활동입니다.", 404L);
        }

        Message resBody = new Message();
        resBody.setMessage("불러오기 완료");
        resBody.setData(result);

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 파트너 정보 조회
    @GetMapping("/admin/partnerInfo")
    public ResponseEntity<Message> partnerInfo(@RequestParam(value = "keyword") @Nullable String keyword,
                                               @RequestParam(value = "partnerDetail") @Nullable String partnerDetail) {

        List<PartnerInfoDTO> partnerList = adminService.partnerInfo(keyword, partnerDetail);

        List<HashMap<String, Object>> data = partnerList.stream().map(partner -> {
            HashMap<String, Object> value = new HashMap<>();

            value.put("stdName", partner.getStdName());
            value.put("stdId", partner.getStdId());
            value.put("department", partner.getDepartment());
            value.put("partnerName", partner.getPartnerName());
            value.put("gender", partner.getPartnerGender());
            value.put("partnerBirth", partner.getPartnerBirth());
            value.put("relation", partner.getRelationship());
            value.put("partnerDivision", partner.getPartnerDivision());

            return value;

        }).collect(Collectors.toList());

        Message resBody = new Message();
        resBody.setData(data);
        resBody.setMessage("조회 완료");

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }
}
