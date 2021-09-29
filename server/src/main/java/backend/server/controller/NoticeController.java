package backend.server.controller;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.notice.NoticeListDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.entity.Notice;
import backend.server.exception.ApiException;
import backend.server.exception.noticeService.DataNotFoundInPageException;
import backend.server.message.Message;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUploadService;
import backend.server.service.notice.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;
    private final FileUploadService fileUploadService;
    private final NoticeListService noticeListService;
    private final NoticeDetailService noticeDetailService;
    private final NoticeCreationService noticeCreationService;
    private final NoticeModifyService noticeModifyService;

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
    public ResponseEntity<Message> update(@RequestParam(value = "imageFiles") @Nullable List<MultipartFile> imageFiles,
                                          @RequestParam(value = "attachedFiles") @Nullable List<MultipartFile> attachedFiles,
                                          @RequestParam(value = "title") String title,
                                          @RequestParam(value = "content") String content,
                                          @RequestParam(value = "noticeId") Long noticeId){

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .noticeId(noticeId)
                .title(title)
                .content(content)
                .build();

        String result = noticeService.update(noticeDTO);
        if(result == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다.", 400L);
        }

        fileUploadService.deleteImageFile(noticeId);
        noticeService.deleteImages(noticeId);
        fileUploadService.deleteAttachedFile(noticeId);
        noticeService.deleteAttachedFiles(noticeId);

        if(imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (file.getSize() != 0) {
                    fileUploadService.uploadImage(file, noticeId);
                }
            }
        }

        if(attachedFiles != null) {
            for (MultipartFile file : attachedFiles) {
                if (file.getSize() != 0) {
                    fileUploadService.uploadAttached(file, noticeId);
                }
            }
        }

        Message resBody = new Message();
        resBody.setMessage("수정 완료");
        resBody.setData(noticeId);

        return new ResponseEntity<>(resBody, null, HttpStatus.OK);
    }

    // 공지사항 게시물 삭제
    @GetMapping("/admin/delete")
    public ResponseEntity<Message> delete(@RequestParam(value = "noticeId") Long noticeId) {

        fileUploadService.deleteImageFile(noticeId);
        fileUploadService.deleteAttachedFile(noticeId);

        String result = noticeService.delete(noticeId);

        if(result == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다.", 400L);
        }

        Message resBody = new Message();
        resBody.setMessage("삭제 완료");
        resBody.setData(noticeId);

        return new ResponseEntity<>(resBody,null,HttpStatus.OK);
    }
}
