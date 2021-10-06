package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.s3.fileUpload.NoticeAttachedFileUploadDTO;
import backend.server.DTO.s3.fileUpload.NoticeImageFileUploadDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeCreationService {

    private final NoticeImagesRepository noticeImagesRepository;
    private final NoticeAttachedFilesRepository noticeAttachedFilesRepository;
    private final NoticeRepository noticeRepository;
    private final FileUploadService fileUploadService;

    // 게시물 DB에 저장
    public Long saveNotice(NoticeDTO.NoticeCreationReqDTO noticeCreationReqDTO) {
        Notice notice = noticeCreationReqDTO.toNotice();
        noticeRepository.save(notice);

        if (noticeCreationReqDTO.isImageFilesPresent()) {
            noticeCreationReqDTO.getImageFiles().forEach(imageFile -> {
                if (imageFile.getSize()!= 0) {
                    NoticeImageFileUploadDTO noticeImageFileUploadDTO = noticeCreationReqDTO.ImageFileToFileUploadDTO();
                    String fileUrl = fileUploadService.uploadFileToS3(noticeImageFileUploadDTO);
                    saveNoticeImages(fileUrl, noticeImageFileUploadDTO.getFileName(), notice.getNoticeId());
                }
            });
        }

        if (noticeCreationReqDTO.isAttachedFilesPresent()) {
            noticeCreationReqDTO.getAttachedFiles().forEach(attachedFile -> {
                if (attachedFile.getSize() != 0) {
                    NoticeAttachedFileUploadDTO noticeAttachedFileUploadDTO = noticeCreationReqDTO.AttachedFileToFileUploadDTO();
                    String fileUrl = fileUploadService.uploadFileToS3(noticeAttachedFileUploadDTO);
                    saveNoticeAttachedFiles(fileUrl, noticeAttachedFileUploadDTO.getFileName(), notice.getNoticeId());
                }
            });
        }

        return notice.getNoticeId();
    }

    private void saveNoticeImages(String fileUrl, String fileName, Long noticeId) {
            NoticeImages noticeImage = NoticeImages.builder()
                    .noticeId(noticeId)
                    .noticeImageUrl(fileUrl)
                    .noticeImageName(fileName)
                    .build();

            noticeImagesRepository.save(noticeImage);
    }

    private void saveNoticeAttachedFiles(String fileUrl, String fileName, Long noticeId) {
            NoticeAttachedFiles noticeAttachedFile = NoticeAttachedFiles.builder()
                    .noticeId(noticeId)
                    .noticeAttachedFilesUrl(fileUrl)
                    .noticeAttachedFileName(fileName)
                    .build();

            noticeAttachedFilesRepository.save(noticeAttachedFile);
    }
}
