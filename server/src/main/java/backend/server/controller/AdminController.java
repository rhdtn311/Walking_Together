package backend.server.controller;

import backend.server.DTO.admin.ActivityDetailInfoDTO;
import backend.server.DTO.admin.ActivityInfoDTO;
import backend.server.DTO.admin.MemberInfoDTO;
import backend.server.DTO.admin.PartnerInfoDTO;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.AdminService;
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

    // 학생정보조회
    @GetMapping("/admin/userinfo")
    public ResponseEntity<Message> userInfo(@RequestParam(value = "keyword") @Nullable String keyword) {

        List<MemberInfoDTO> stdList = adminService.userInfo(keyword);

        List<HashMap<String, Object>> data = stdList.stream().map(student -> {
            HashMap<String, Object> value = new HashMap<>();

            value.put("name", student.getStdName());
            value.put("stdId", student.getStdId());
            value.put("department", student.getStdDepartment());
            value.put("email", student.getStdEmail());
            value.put("birth", student.getStdBirth());
            value.put("pNumber", student.getPNumber());

            return value;

        }).collect(Collectors.toList());

        Message resBody = new Message();
        resBody.setMessage("조회 완료");
        resBody.setData(data);

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 활동정보조회
    @GetMapping("/admin/activityInfo")
    public ResponseEntity<Message> activityInfo(@RequestParam(value = "keyword") @Nullable String keyword,
                                                @RequestParam(value = "from") @Nullable String from,
                                                @RequestParam(value = "to") @Nullable String to,
                                                @RequestParam(value = "activityDivision") @Nullable int activityDivision) {
        LocalDate fromDate = LocalDate.of(2020, 01, 01);
        LocalDate toDate = LocalDate.of(2030, 01, 01);

        if (from != null || to != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            fromDate = LocalDate.parse(from, formatter);
            toDate = LocalDate.parse(to, formatter);
        }

        List<ActivityInfoDTO> activityList = adminService.activityInfo(keyword, fromDate, toDate, activityDivision);

        List<HashMap<String, Object>> data = activityList.stream().map(activity -> {
            HashMap<String, Object> value = new HashMap<>();

            String activityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String startTime = activity.getActivityStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String endTime = null;
            if (activity.getEndTime() != null) {
                endTime = activity.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            }

            value.put("stdName", activity.getStdName());
            value.put("department", activity.getDepartment());
            value.put("stdId", activity.getStdId());
            value.put("activityDate", activityDate);
            value.put("startTime", startTime);
            value.put("activityId", activity.getActivityId());
            value.put("endTime", endTime);
            value.put("totalDistance", activity.getDistance());
            value.put("partnerName", activity.getPartnerName());

            return value;

        }).collect(Collectors.toList());

        Message resBody = new Message();
        resBody.setData(data);
        resBody.setMessage("조회 완료");

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
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
