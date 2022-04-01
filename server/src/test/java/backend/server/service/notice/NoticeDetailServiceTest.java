package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class NoticeDetailServiceTest {

    @Autowired
    NoticeDetailService noticeDetailService;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    @Autowired
    NoticeImagesRepository noticeImagesRepository;

    Notice notice;
    NoticeAttachedFiles noticeAttachedFile1;
    NoticeAttachedFiles noticeAttachedFile2;
    NoticeImages noticeImage;

    @BeforeEach
    void init() {
        notice = Notice.builder()
                .title("title")
                .content("content")
                .build();
        noticeRepository.save(notice);

        noticeAttachedFile1 = NoticeAttachedFiles.builder()
                .noticeId(notice.getNoticeId())
                .noticeAttachedFileName("noticeAttachedFileName1")
                .noticeAttachedFilesUrl("noticeAttachedFilesURL1")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile1);

        noticeAttachedFile2 = NoticeAttachedFiles.builder()
                .noticeId(notice.getNoticeId())
                .noticeAttachedFileName("noticeAttachedFileName2")
                .noticeAttachedFilesUrl("noticeAttachedFilesURL2")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile2);

        noticeImage = NoticeImages.builder()
                .noticeId(notice.getNoticeId())
                .noticeImageName("noticeImageName")
                .noticeImageUrl("noticeImageURL")
                .build();
        noticeImagesRepository.save(noticeImage);
    }

    @Test
    @DisplayName("게시물 상세보기 확인")
    void getNoticeDetail() {

        // when
        NoticeDTO.NoticeDetailResDTO noticeDetail = noticeDetailService.getNoticeDetail(notice.getNoticeId());

        // then
        assertThat(noticeDetail.getNoticeId()).isEqualTo(notice.getNoticeId());
        assertThat(noticeDetail.getTitle()).isEqualTo(notice.getTitle());
        assertThat(noticeDetail.getContent()).isEqualTo(notice.getContent());
        assertThat(noticeDetail.getCreateTime()).isEqualTo(notice.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(noticeDetail.getAttachedFiles().get(0)).isEqualTo(noticeAttachedFile1.getNoticeAttachedFilesUrl());
        assertThat(noticeDetail.getAttachedFiles().get(1)).isEqualTo(noticeAttachedFile2.getNoticeAttachedFilesUrl());
        assertThat(noticeDetail.getImageFiles().get(0)).isEqualTo(noticeImage.getNoticeImageUrl());
    }

}