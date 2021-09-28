package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.entity.Notice;
import backend.server.repository.NoticeRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeCreationService {

    private final NoticeRepository noticeRepository;
    private final FileUploadService fileUploadService;

    // 게시물 DB에 저장
    public Long saveNotice(NoticeDTO.NoticeCreationReqDTO noticeCreationReqDTO) {
        Notice notice = noticeCreationReqDTO.toNotice();
        noticeRepository.save(notice);

        if (noticeCreationReqDTO.isImageFilesPresent()) {
            noticeCreationReqDTO.getImageFiles().forEach(imageFile -> {
                if (imageFile.getSize()!= 0) {
                    fileUploadService.uploadImage(imageFile, notice.getNoticeId());
                }
            });
        }

        if (noticeCreationReqDTO.isAttachedFilesPresent()) {
            noticeCreationReqDTO.getAttachedFiles().forEach(attachedFile -> {
                if (attachedFile.getSize() != 0) {
                    fileUploadService.uploadAttached(attachedFile, notice.getNoticeId());
                }
            });
        }

        return notice.getNoticeId();
    }
}
