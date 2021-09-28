package backend.server.service.notice;

import backend.server.DTO.notice.NoticeAttachedFilesDTO;
import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.notice.NoticeImagesDTO;
import backend.server.DTO.notice.NoticeListDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final NoticeQueryRepository noticeQueryRepository;

    private final FileUploadService fileUploadService;

    private NoticeListDTO entityToDto(Notice notice) {

        NoticeListDTO dto = NoticeListDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .date(notice.getModDate())
                .build();

        return dto;
    }

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

    public NoticeListDTO detailNotice(Long noticeId) {
        Optional<Notice> notice = noticeRepository.findNoticeByNoticeId(noticeId);

        if(notice.isEmpty()) {
            return null;
        }
        Notice entity = notice.get();

        NoticeListDTO dto = entityToDto(entity);

        return dto;
    }
//
    // 공지사항 게시물 상세 (Files and Images)
    public List<ArrayList<String>> detailNoticeFiles(Long noticeId) {

        List<NoticeImages> noticeImages = imagesRepository.findNoticeImagesByNoticeId(noticeId);
        NoticeImagesDTO imagesDTO = NoticeImagesDTO.builder()
                .noticeImages(noticeImages)
                .build();

        List<NoticeAttachedFiles> noticeFiles = attachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);
        NoticeAttachedFilesDTO filesDTO = NoticeAttachedFilesDTO.builder()
                .noticeFiles(noticeFiles)
                .build();

        ArrayList<String> noticeImagesURL = new ArrayList<>();
        ArrayList<String> noticeFilesURL = new ArrayList<>();

        imagesDTO.getNoticeImages().forEach(
                i -> {
                    noticeImagesURL.add(i.getNoticeImageUrl());
                }
        );

        filesDTO.getNoticeFiles().forEach(
                i -> {
                    noticeFilesURL.add(i.getNoticeAttachedFilesUrl());
                }
        );

        List<ArrayList<String>> result = new ArrayList<>();
        result.add(noticeImagesURL);
        result.add(noticeFilesURL);

        return result;
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

    // 게시물 수정 (title, content)
    public String update(NoticeDTO dto) {

        Optional<Notice> optNotice = noticeRepository.findById(dto.getNoticeId());

        if(optNotice.isEmpty()) {
            return null;
        }

        Notice notice = optNotice.get();

        notice.changeTitle(dto.getTitle());
        notice.changeContent(dto.getContent());

        noticeRepository.save(notice);

        return "pass";
    }

    public void deleteImages(Long noticeId) {
        List<NoticeImages> noticeImages = imagesRepository.findNoticeImagesByNoticeId(noticeId);

        noticeImages.forEach(imagesRepository::delete);
    }

    public void deleteAttachedFiles(Long noticeId) {
        List<NoticeAttachedFiles> noticeAttachedFiles = attachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);

        noticeAttachedFiles.forEach(attachedFilesRepository::delete);

    }

}
