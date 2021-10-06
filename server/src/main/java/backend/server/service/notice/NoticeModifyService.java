package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.s3.fileUpload.NoticeAttachedFileUploadDTO;
import backend.server.DTO.s3.fileUpload.NoticeImageFileUploadDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.exception.noticeService.NoticeNotFoundException;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUpdateService;
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
    private final NoticeImagesRepository noticeImagesRepository;
    private final NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    private final FileUpdateService fileUpdateService;

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
                NoticeAttachedFileUploadDTO fileUploadDTO = new NoticeAttachedFileUploadDTO(file);
                String fileUrl = fileUpdateService.updateFile(fileUploadDTO, noticeAttachedFilesRepository, noticeId);
                updateNoticeAttachedFilesInDB(fileUrl, fileUploadDTO.getFileName(), noticeId);
            }
        });
    }

    public void updateImages(List<MultipartFile> images, Long noticeId) {
        images.forEach(image -> {
            if (image.getSize() != 0) {
                NoticeImageFileUploadDTO fileUploadDTO = new NoticeImageFileUploadDTO(image);
                String fileUrl = fileUpdateService.updateFile(fileUploadDTO, noticeImagesRepository, noticeId);
                updateNoticeImagesInDB(fileUrl, fileUploadDTO.getFileName(), noticeId);
            }
        });
    }

    private void updateNoticeImagesInDB(String fileUrl, String fileName, Long noticeId) {
        if (noticeImagesRepository.existsNoticeImagesByNoticeId(noticeId)) {
            NoticeImages noticeImage = noticeImagesRepository.findFirstByNoticeId(noticeId);
            noticeImage.changeImageName(fileName);
            noticeImage.changeImageUrl(fileUrl);
        } else {
        NoticeImages noticeImage = NoticeImages.builder()
                .noticeId(noticeId)
                .noticeImageUrl(fileUrl)
                .noticeImageName(fileName)
                .build();

        noticeImagesRepository.save(noticeImage);
        }
    }

    private void updateNoticeAttachedFilesInDB(String fileUrl, String fileName, Long noticeId) {
        if (noticeAttachedFilesRepository.existsNoticeAttachedFilesByNoticeId(noticeId)) {
            NoticeAttachedFiles noticeAttachedFile = noticeAttachedFilesRepository.findFirstByNoticeId(noticeId);
            noticeAttachedFile.changeFileName(fileName);
            noticeAttachedFile.changeFileUrl(fileUrl);
        } else {
        NoticeAttachedFiles noticeAttachedFile = NoticeAttachedFiles.builder()
                .noticeId(noticeId)
                .noticeAttachedFilesUrl(fileUrl)
                .noticeAttachedFileName(fileName)
                .build();

        noticeAttachedFilesRepository.save(noticeAttachedFile);
        }
    }
}
