package backend.server.controller;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.myPage.MyPagePartnerDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.mypage.*;
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

    private final MyPageMainService myPageMainService;
    private final MyPageChangeService myPageChangeService;
    private final MyPagePartnerInfoService myPagePartnerInfoService;
    private final PartnerCreationService partnerCreationService;
    private final PartnerInfoChangeService partnerInfoChangeService;
    private final PartnerDeleteService partnerDeleteService;

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
    public ResponseEntity<ResponseDTO> getMyPagePartnerDetail(@RequestParam(value = "partnerId") Long partnerId) {
        MyPageDTO.MyPagePartnerDetailResDTO myPagePartnerDetail = myPagePartnerInfoService.getMyPagePartnerDetail(partnerId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너 세부 조회 완료")
                .data(myPagePartnerDetail)
                .build());
    }

    // 마이페이지 - 파트너 생성
    @PostMapping("/partner/create")
    public ResponseEntity<ResponseDTO> createPartner(MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO) {
        Long savedPartnerId = partnerCreationService.createPartner(partnerCreationReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너 저장 완료")
                .data(savedPartnerId)
                .build());
    }

    // 마이페이지 - 파트너 정보 수정
    @PostMapping("/partner/change")
    public ResponseEntity<ResponseDTO> changePartnerInfo(MyPageDTO.PartnerInfoChangeReqDTO partnerInfoChangeReqDTO) {
        Long partnerId = partnerInfoChangeService.changePartnerInfo(partnerInfoChangeReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너 정보가 성공적으로 변경되었습니다.")
                .data(partnerId)
                .build());
    }

    // 마이페이지 - 파트너 삭제
    @GetMapping("/partner/delete")
    public ResponseEntity<ResponseDTO> deletePartner(@RequestParam(value = "partnerId") Long partnerId) {
        Long deletePartnerId = partnerDeleteService.deletePartner(partnerId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("파트너를 성공적으로 삭제했습니다.")
                .data(deletePartnerId)
                .build());
    }
}








