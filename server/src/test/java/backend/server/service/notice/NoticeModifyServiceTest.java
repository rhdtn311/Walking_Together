package backend.server.service.notice;

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
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeModifyServiceTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeModifyService noticeModifyService;

    @Autowired
    NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    @Autowired
    NoticeImagesRepository noticeImagesRepository;

    Notice notice;
    NoticeAttachedFiles noticeAttachedFile;
    NoticeImages noticeImage;

    @BeforeEach
    void init() {
        notice = Notice.builder()
                .title("title")
                .content("content")
                .build();
        noticeRepository.save(notice);

        noticeAttachedFile = NoticeAttachedFiles.builder()
                .noticeId(notice.getNoticeId())
                .noticeAttachedFileName("noticeAttachedFileName")
                .noticeAttachedFilesUrl("noticeAttachedFileURL")
                .build();
        noticeAttachedFilesRepository.save(noticeAttachedFile);

        noticeImage = NoticeImages.builder()
                .noticeId(notice.getNoticeId())
                .noticeImageName("noticeImageName")
                .noticeImageUrl("noticeImageURL")
                .build();
        noticeImagesRepository.save(noticeImage);
    }

    @Test
    @DisplayName("공지사항 수정 시 내용 불러오기")
    void getNoticeInfo() {

        // when
        NoticeDTO.NoticeModifyResDTO noticeInfo = noticeModifyService.getNoticeInfo(notice.getNoticeId());

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo(notice.getTitle());
        assertThat(noticeInfo.getContent()).isEqualTo(notice.getContent());
        assertThat(noticeInfo.getAttachedFiles().get(0)).isEqualTo(noticeAttachedFile.getNoticeAttachedFilesUrl());
        assertThat(noticeInfo.getImageFiles().get(0)).isEqualTo(noticeImage.getNoticeImageUrl());
    }

    @Test
    @DisplayName("공지사항 수정 확인")
    void modifyNoticeTitle() {

        // given

        NoticeDTO.NoticeModifyReqDTO noticeModifyReqDTO = NoticeDTO.NoticeModifyReqDTO.builder()
                .noticeId(notice.getNoticeId())
                .title("new title")
                .content("new content")
                .build();

        // when
        noticeModifyService.modifyNoticeInfo(noticeModifyReqDTO);
        Notice findNotice = noticeRepository.findById(this.notice.getNoticeId()).get();

        // then
        assertThat(findNotice.getTitle()).isEqualTo("new title");
        assertThat(findNotice.getContent()).isEqualTo("new content");
    }

}