package backend.server.repository.querydsl;

import backend.TestConfig;
import backend.server.DTO.notice.NoticeDTO;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({TestConfig.class, NoticeQueryRepository.class})
@DataJpaTest
class NoticeQueryRepositoryTest {

    @Autowired
    private NoticeQueryRepository noticeQueryRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeImagesRepository noticeImagesRepository;

    @Autowired
    private NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    List<Notice> noticeList = new ArrayList<>();

    @BeforeEach
    void init() {
        for (int i = 0; i < 20; i++) {
            Notice notice = noticeRepository.save(
                    Notice.builder()
                            .title("title" + i)
                            .content("content" + i)
                            .build()
            );
            noticeList.add(notice);
        }

        NoticeImages noticeImage = NoticeImages.builder()
                .noticeId(1L)
                .noticeImageUrl("noticeImageURL")
                .noticeImageName("noticeImageName")
                .build();
        noticeImagesRepository.save(noticeImage);

        NoticeAttachedFiles noticeAttachedFile = NoticeAttachedFiles.builder()
                .noticeId(1L)
                .noticeAttachedFilesUrl("noticeAttachedFileURL")
                .noticeAttachedFileName("noticeAttachedFileName")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile);
    }

    @Test
    @DisplayName("게시글 세부 조회")
    void findNoticeDetail() {

        // given
        Long noticeId = noticeList.get(0).getNoticeId();
        Notice notice = noticeRepository.findById(noticeId).get();

        // when
        NoticeDTO.NoticeDetailResDTO noticeDetailResDTO = noticeQueryRepository.findNoticeDetail(noticeId);

        // then
        assertThat(noticeDetailResDTO.getNoticeId()).isEqualTo(noticeId);
        assertThat(noticeDetailResDTO.getTitle()).isEqualTo(notice.getTitle());
        assertThat(noticeDetailResDTO.getContent()).isEqualTo(notice.getContent());
    }

    @Test
    @DisplayName("게시글 수정 조회")
    void findNoticeModify() {
        // given
        Long noticeId = noticeList.get(0).getNoticeId();
        Notice notice = noticeRepository.findById(noticeId).get();

        // when
        NoticeDTO.NoticeModifyResDTO noticeModifyResDTO = noticeQueryRepository.findNoticeModify(noticeId);

        // then
        assertThat(noticeModifyResDTO.getTitle()).isEqualTo(notice.getTitle());
        assertThat(noticeModifyResDTO.getContent()).isEqualTo(notice.getContent());
    }

    @Test
    @DisplayName("게시글 첨부파일 조회")
    void findNoticeAttachedFileUrls() {
        // given
        Long noticeId = 1L;
        NoticeAttachedFiles noticeAttachedFile = noticeAttachedFilesRepository.findFirstByNoticeId(noticeId);

        // when
        List<String> noticeAttachedFileURLs = noticeQueryRepository.findNoticeAttachedFileURLs(noticeId);

        // then
        assertThat(noticeAttachedFileURLs.size()).isEqualTo(1);
        assertThat(noticeAttachedFileURLs.get(0)).isEqualTo(noticeAttachedFile.getNoticeAttachedFilesUrl());
    }

    @Test
    @DisplayName("게시글 이미지 조회")
    void findNoticeImageFileUrls() {
        // given
        Long noticeId = 1L;
        Notice findNotice = noticeList.get(0);
        NoticeImages noticeImageFile = noticeImagesRepository.findFirstByNoticeId(findNotice.getNoticeId());

        // when
        List<String> noticeImageFileURLs = noticeQueryRepository.findNoticeImageFileURLs(findNotice.getNoticeId());

        // then
        assertThat(noticeImageFileURLs.size()).isEqualTo(1);
        assertThat(noticeImageFileURLs.get(0)).isEqualTo(noticeImageFile.getNoticeImageUrl());

    }
}