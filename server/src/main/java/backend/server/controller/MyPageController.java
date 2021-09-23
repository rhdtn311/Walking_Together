package backend.server.controller;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.myPage.MyPagePartnerDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.mypage.MyPageChangeService;
import backend.server.service.mypage.MyPageMainService;
import backend.server.service.mypage.MyPagePartnerInfoService;
import backend.server.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;
    private final MyPageMainService myPageMainService;
    private final MyPageChangeService myPageChangeService;
    private final MyPagePartnerInfoService myPagePartnerInfoService;

    @GetMapping("/mypage")
    public ResponseEntity<ResponseDTO> getMyPageMain(@RequestParam(value = "stdId") String stdId) {
        MyPageDTO.MyPageMainResDTO member = myPageMainService.getMyPageMain(stdId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("조회 성공")
                .data(member)
                .build());
    }

    // 마이페이지-변경
    @PostMapping("/mypage/change")
    public ResponseEntity<ResponseDTO> changeMemberInfo (MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO) {
        String changeMemberStdId = myPageChangeService.changeMemberInfo(myPageChangeReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("회원 정보 수정 완료")
                .data(changeMemberStdId)
                .build());
    }

    // 마이페이지 - 파트너
    @GetMapping("/mypage/partnerInfo")
    public ResponseEntity<ResponseDTO> myPagePartnerInfo(@RequestParam(value = "stdId") String stdId) {
        List<MyPageDTO.MyPagePartnerListResDTO> partnerList = myPagePartnerInfoService.getPartnerList(stdId);
        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너 리스트 출력 완료")
                .data(partnerList)
                .build());
    }

    // 마이페이지 -파트너 detail
    @GetMapping("/mypage/partnerInfo/detail")
    public ResponseEntity<Message> myPagePartnerDetail(@RequestParam(value = "partnerId") Long partnerId) {

        MyPagePartnerDTO myPagePartnerInfo = myPageService.myPagePartnerDetail(partnerId);

        if(myPagePartnerInfo == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "파트너가 존재하지 않습니다.", 400L);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("partnerName", myPagePartnerInfo.getPartnerName());
        data.put("partnerDetail", myPagePartnerInfo.getPartnerDetail());
        data.put("partnerBirth", myPagePartnerInfo.getPartnerBirth());
        data.put("gender", myPagePartnerInfo.getGender());
        data.put("selectionReason", myPagePartnerInfo.getSelectionReason());
        data.put("relationship", myPagePartnerInfo.getRelationship());

        Message resBody = new Message();
        resBody.setMessage("파트너 세부 조회 완료");
        resBody.setData(data);

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 마이페이지 - 파트너 생성
    @PostMapping("/partner/create")
    public ResponseEntity<Message> createPartner(@RequestParam(value = "stdId") String stdId,
                                             @RequestParam(value = "partnerName") String partnerName,
                                             @RequestParam(value = "partnerDetail") String partnerDetail,
                                             @RequestParam(value = "partnerPhoto") MultipartFile partnerPhoto,
                                             @RequestParam(value = "selectionReason") String selectionReason,
                                             @RequestParam(value = "relationship") String relationship,
                                             @RequestParam(value = "gender") String gender,
                                             @RequestParam(value = "partnerBirth") String partnerBirth) {

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer birth = stringBuffer.append(partnerBirth);
        birth.replace(4,5,"/");
        birth.replace(7,8,"/");

        MyPagePartnerDTO savePartner = MyPagePartnerDTO.builder()
                .stdId(stdId)
                .partnerName(partnerName)
                .partnerDetail(partnerDetail)
                .partnerBirth(birth.toString())
                .selectionReason(selectionReason)
                .relationship(relationship)
                .gender(gender)
                .build();

        Long result = myPageService.createPartner(savePartner, partnerPhoto);
        if(result == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.", 400L);
        }

        Message resBody = new Message();
        resBody.setMessage("파트너 저장 완료");

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 마이페이지 - 파트너 정보 수정
    @PostMapping("/partner/change")
    public ResponseEntity<Message> changePartner(@RequestParam(value = "partnerId") String partnerId,
                              @RequestParam(value = "partnerName") @Nullable String partnerName,
                              @RequestParam(value = "partnerDetail") @Nullable String partnerDetail,
                              @RequestParam(value = "selectionReason") @Nullable String selectionReason,
                              @RequestParam(value = "relationship") @Nullable String relationship,
                              @RequestParam(value = "gender") @Nullable String gender,
                              @RequestParam(value = "partnerBirth") @Nullable String partnerBirth,
                              @RequestParam(value = "partnerPhoto") @Nullable MultipartFile partnerPhoto)
    {
        Long partnerIdU = Long.parseLong(partnerId);

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer birth = stringBuffer.append(partnerBirth);

        birth.replace(4,5,"/");
        birth.replace(7,8,"/");

        MyPagePartnerDTO updatePartner = MyPagePartnerDTO.builder()
                .partnerId(partnerIdU)
                .partnerName(partnerName)
                .partnerDetail(partnerDetail)
                .partnerBirth(birth.toString())
                .selectionReason(selectionReason)
                .relationship(relationship)
                .gender(gender)
                .build();

        Long updateResult = myPageService.updatePartner(updatePartner, partnerPhoto);

        if(updateResult == null) {
            throw new ApiException(HttpStatus.NOT_FOUND,"존재하지 않는 파트너입니다.", 400L);
        }

        Message resBody = new Message();
        resBody.setMessage("파트너 정보가 성공적으로 변경되었습니다.");
        resBody.setData(partnerId);

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 마이페이지 - 파트너 삭제
    @GetMapping("/partner/delete")
    public ResponseEntity<Message> deletePartner(@RequestParam(value = "partnerId") Long partnerId) {

        Long result = myPageService.deletePartner(partnerId);

        Message resBody = new Message();
        resBody.setMessage("파트너를 성공적으로 삭제했습니다.");
        resBody.setData(partnerId);

        if (result == 400L) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "활동을 가지고 있는 파트너입니다.", 400L);
        }

        if (result == 404L) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 파트너입니다.", 404L);
        }

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }
}
