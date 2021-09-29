package backend.server.service.notice;

import backend.server.DTO.notice.NoticeAttachedFilesDTO;
import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.notice.NoticeImagesDTO;
import backend.server.DTO.notice.NoticeListDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.exception.noticeService.NoticeNotFoundException;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeImagesRepository imagesRepository;
    private final NoticeAttachedFilesRepository attachedFilesRepository;
    private final FileUploadService fileUploadService;
    private final NoticeQueryRepository noticeQueryRepository;

    public void deleteImages(Long noticeId) {
        List<NoticeImages> noticeImages = imagesRepository.findNoticeImagesByNoticeId(noticeId);

        noticeImages.forEach(imagesRepository::delete);
    }

    public void deleteAttachedFiles(Long noticeId) {
        List<NoticeAttachedFiles> noticeAttachedFiles = attachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);

        noticeAttachedFiles.forEach(attachedFilesRepository::delete);

    }

    // 공지사항 게시글 삭제
    public String delete(Long noticeId) {

        Optional<Notice> findNotice = noticeRepository.findById(noticeId);

        if(findNotice.isEmpty()) {
            return null;
        }

        noticeRepository.delete(findNotice.get());

        deleteImages(noticeId);
        deleteAttachedFiles(noticeId);

        return "pass";
    }


}
