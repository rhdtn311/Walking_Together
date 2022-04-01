package backend.server.service.notice;

import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeDeleteServiceTest {

    @Autowired
    NoticeDeleteService noticeDeleteService;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    @Autowired
    NoticeImagesRepository noticeImagesRepository;

    Notice notice;

    @BeforeEach
    void init() {
        notice = Notice.builder()
                .title("title")
                .content("content")
                .build();
        noticeRepository.save(notice);

        NoticeAttachedFiles noticeAttachedFile1 = NoticeAttachedFiles.builder()
                .noticeId(notice.getNoticeId())
                .noticeAttachedFileName("noticeAttachedFileName1")
                .noticeAttachedFilesUrl("noticeAttachedFilesURL1")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile1);

        NoticeAttachedFiles noticeAttachedFile2 = NoticeAttachedFiles.builder()
                .noticeId(notice.getNoticeId())
                .noticeAttachedFileName("noticeAttachedFileName2")
                .noticeAttachedFilesUrl("noticeAttachedFilesURL2")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile2);

        NoticeImages noticeImage = NoticeImages.builder()
                .noticeId(notice.getNoticeId())
                .noticeImageName("noticeImageName")
                .noticeImageUrl("noticeImageURL")
                .build();
        noticeImagesRepository.save(noticeImage);
    }

    @Test
    @DisplayName("게시물 삭제 확인")
    void deleteNotice() {
        // when
        noticeDeleteService.deleteNotice(notice.getNoticeId());

        // then
        assertThat(noticeRepository.findById(notice.getNoticeId())).isEmpty();
        assertThat(noticeImagesRepository.findNoticeImagesByNoticeId(notice.getNoticeId())).isEmpty();
        assertThat(noticeAttachedFilesRepository.findNoticeAttachedFilesByNoticeId(notice.getNoticeId())).isEmpty();
    }
}