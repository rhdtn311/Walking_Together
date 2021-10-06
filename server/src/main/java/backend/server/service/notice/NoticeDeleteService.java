package backend.server.service.notice;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.exception.noticeService.NoticeNotFoundException;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.s3.FileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeDeleteService {

    private final NoticeRepository noticeRepository;
    private final NoticeImagesRepository noticeImagesRepository;
    private final NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    private final FileDeleteService fileDeleteService;

    public Long deleteNotice(Long noticeId) {
        Optional<Notice> noticeOptional = noticeRepository.findById(noticeId);
        if (noticeOptional.isEmpty()) {
            throw new NoticeNotFoundException();
        }
        Notice notice = noticeOptional.get();
        noticeRepository.delete(notice);

        fileDeleteService.deleteFile(noticeImagesRepository, new FileDeleteDTO(noticeId));
        fileDeleteService.deleteFile(noticeAttachedFilesRepository, new FileDeleteDTO(noticeId));

        deleteNoticeImages(noticeId);
        deleteNoticeAttachedFiles(noticeId);

        return notice.getNoticeId();
    }

    public void deleteNoticeImages(Long noticeId) {
        List<NoticeImages> noticeImages = noticeImagesRepository.findNoticeImagesByNoticeId(noticeId);

        noticeImages.forEach(noticeImagesRepository::delete);
    }

    public void deleteNoticeAttachedFiles(Long noticeId) {
        List<NoticeAttachedFiles> noticeAttachedFiles = noticeAttachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);

        noticeAttachedFiles.forEach(noticeAttachedFilesRepository::delete);
    }
}
