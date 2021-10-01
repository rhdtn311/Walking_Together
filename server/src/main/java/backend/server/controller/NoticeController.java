package backend.server.controller;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.entity.Notice;
import backend.server.exception.noticeService.DataNotFoundInPageException;
import backend.server.service.notice.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeListService noticeListService;
    private final NoticeDetailService noticeDetailService;
    private final NoticeCreationService noticeCreationService;
    private final NoticeModifyService noticeModifyService;
    private final NoticeDeleteService noticeDeleteService;

    // 공지사항 게시물 출력
    @PostMapping("/noticeList")
    public ResponseEntity<ResponseDTO> getNoticeList(@RequestBody PageRequestDTO pageRequestDTO) {
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> pageResultDTO = noticeListService.findNoticeList(pageRequestDTO);
        if(pageResultDTO.getTotalPage() < pageRequestDTO.getPage()) {
            throw new DataNotFoundInPageException();
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("게시글 조회 완료")
                .data(pageResultDTO)
                .build());
    }

    // 공지사항 게시물 상세
    @GetMapping("/notice")
    public ResponseEntity<ResponseDTO> detailNotice(@RequestParam(value = "noticeId") Long noticeId) {
        NoticeDTO.NoticeDetailResDTO noticeDetail = noticeDetailService.getNoticeDetail(noticeId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("공지사항 게시물 조회 완료")
                .data(noticeDetail)
                .build());
    }

    // 공지사항 등록 (이미지, 첨부파일 받아서 다른 DB에 각각 저장)
    @PostMapping("/admin/createpost")
    public ResponseEntity<ResponseDTO> createNotice(NoticeDTO.NoticeCreationReqDTO noticeCreationReqDTO) {
        Long noticeId = noticeCreationService.saveNotice(noticeCreationReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("게시글 업로드 완료")
                .data(noticeId)
                .build());
    }

    // 공지사항 게시물 수정
    @GetMapping("/admin/update")
    public ResponseEntity<ResponseDTO> modifyNotice(@RequestParam(value = "noticeId") Long noticeId) {
        NoticeDTO.NoticeModifyResDTO noticeModifyResDTO = noticeModifyService.getNoticeInfo(noticeId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("게시글 조회 완료")
                .data(noticeModifyResDTO)
                .build());
    }

    // 공지사항 게시물 수정
    @PostMapping("/admin/update")
    public ResponseEntity<ResponseDTO> modifyNoticeInfo(NoticeDTO.NoticeModifyReqDTO noticeModifyReqDTO){
        Long noticeId = noticeModifyService.modifyNoticeInfo(noticeModifyReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("게시글 수정 완료")
                .data(noticeId)
                .build());
    }

    // 공지사항 게시물 삭제
    @GetMapping("/admin/delete")
    public ResponseEntity<ResponseDTO> deleteNotice(@RequestParam(value = "noticeId") Long noticeId) {
        Long deleteNotice = noticeDeleteService.deleteNotice(noticeId);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("게시물 삭제 성공")
                .data(deleteNotice)
                .build());
    }
}
