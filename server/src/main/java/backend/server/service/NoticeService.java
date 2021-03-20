package backend.server.service;

import backend.server.DTO.notice.NoticeAttachedFilesDTO;
import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.notice.NoticeImagesDTO;
import backend.server.DTO.notice.NoticeListDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.entity.QNotice;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.s3.S3Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final S3Service s3Service;
    private final NoticeRepository noticeRepository;
    private final NoticeImagesRepository imagesRepository;
    private final NoticeAttachedFilesRepository attachedFilesRepository;

    public PageResultDTO<NoticeListDTO, Notice> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("noticeId").descending());

        // 검색 조건 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Notice> result = noticeRepository.findAll(booleanBuilder, pageable);

        // Function<input type, result type>
        Function<Notice, NoticeListDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    // 검색 처리(where)
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QNotice qNotice = QNotice.notice;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qNotice.noticeId.gt(0L);

        booleanBuilder.and(expression);

        // 검색 조건
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(keyword != null) {
            conditionBuilder.or(qNotice.content.contains(keyword));
            conditionBuilder.or(qNotice.title.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

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
    public Long saveNotice(NoticeDTO dto) {
        Notice notice = dto.dtoToEntity();
        noticeRepository.save(notice);
        return notice.getNoticeId();
    }

    // 공지사항 게시물 상세 (Notice 받아오기)
    public NoticeListDTO detailNotice(Long noticeId) {

        Optional<Notice> notice = noticeRepository.findNoticeByNoticeId(noticeId);

        if(notice.isEmpty()) {
            return null;
        }
        Notice entity = notice.get();

        NoticeListDTO dto = entityToDto(entity);

        return dto;
    }

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
