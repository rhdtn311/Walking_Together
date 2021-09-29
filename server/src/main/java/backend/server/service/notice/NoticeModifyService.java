package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.entity.Notice;
import backend.server.exception.noticeService.NoticeNotFoundException;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeModifyService {

    private final NoticeRepository noticeRepository;

    private final NoticeQueryRepository noticeQueryRepository;

    private final FileUploadService fileUploadService;

    public NoticeDTO.NoticeModifyResDTO getNoticeInfo(Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new NoticeNotFoundException();
        }

        NoticeDTO.NoticeModifyResDTO noticeModifyResDTO = noticeQueryRepository.findNoticeModify(noticeId);
        List<String> noticeAttachedFileURLs = noticeQueryRepository.findNoticeAttachedFileURLs(noticeId);
        List<String> noticeImageFileURLs = noticeQueryRepository.findNoticeImageFileURLs(noticeId);

        noticeModifyResDTO.setAttachedFiles(noticeAttachedFileURLs);
        noticeModifyResDTO.setImageFiles(noticeImageFileURLs);

        return noticeModifyResDTO;
    }

    // 게시물 수정 (title, content)
    @Transactional
    public Long modifyNoticeInfo(NoticeDTO.NoticeModifyReqDTO noticeModifyReqDTO) {
        if (!noticeRepository.existsById(noticeModifyReqDTO.getNoticeId())) {
            throw new NoticeNotFoundException();
        }

        Notice notice = noticeRepository.findNoticeByNoticeId(noticeModifyReqDTO.getNoticeId());
        notice.changeTitle(noticeModifyReqDTO.getTitle());
        notice.changeContent(noticeModifyReqDTO.getContent());

        if (noticeModifyReqDTO.isImageFilesPresent()) {
            updateImages(noticeModifyReqDTO.getImageFiles(), noticeModifyReqDTO.getNoticeId());
        }

        if (noticeModifyReqDTO.isAttachedFilesPresent()) {
            updateFiles(noticeModifyReqDTO.getAttachedFiles(), noticeModifyReqDTO.getNoticeId());
        }

        return noticeModifyReqDTO.getNoticeId();
    }

    public void updateFiles(List<MultipartFile> files, Long noticeId) {
        files.forEach(file -> {
            if (file.getSize() != 0) {
                fileUploadService.updateAttached(file, noticeId);
            }
        });
    }

    public void updateImages(List<MultipartFile> images, Long noticeId) {
        images.forEach(image -> {
            if (image.getSize() != 0) {
                fileUploadService.updateImageFiles(image, noticeId);
            }
        });
    }
}
