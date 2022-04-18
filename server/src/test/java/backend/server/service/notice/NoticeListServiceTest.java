package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.entity.Notice;
import backend.server.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeListServiceTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeListService noticeListService;

    @Rollback(true)
    @BeforeEach
    void init() {

        for (int i = 1; i <= 125; i++) {
            Notice notice = Notice.builder()
                    .title("notice" + i)
                    .content("content" + i)
                    .build();

            noticeRepository.save(notice);
        }
    }

    @Test
    @DisplayName("게시물 조회 시 1페이지부터 끝페이지까지 최신 게시글부터 1페이지에 있는지 확인")
    void findNoticeListSort() {

        // given
        // 1페이지 조회
        int num = 125;
        for (int i = 1; i <= 13; i++) {
            PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                    .keyword("")
                    .page(i)
                    .size(10)
                    .build();

            // when
            PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

            // then
            int[] nums = IntStream.rangeClosed(num - 9, num).toArray();
            for (int j = 0; j < noticeList.getPageDataList().size(); j++) {
                assertThat(noticeList.getPageDataList().get(j).getTitle()).isEqualTo("notice" + nums[9 - j]);
            }

            num -= 10;
        }
    }

    @Test
    @DisplayName("다음 페이지가 존재할 경우 Next값 true 확인")
    void isNext() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(1)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertTrue(noticeList.isNext());
    }

    @Test
    @DisplayName("이전 페이지가 존재할 경우 Prev값 true 확인")
    void isPrev() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(11)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertTrue(noticeList.isPrev());
    }

    @Test
    @DisplayName("다음 페이지가 존재하지 않을 경우 Next값 false 확인")
    void isNextFalse() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(11)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertFalse(noticeList.isNext());
    }

    @Test
    @DisplayName("이전 페이지가 존재하지 않을 경우 prev값 false 확인")
    void isPrevFalse() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(1)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertFalse(noticeList.isPrev());
    }

    @Test
    @DisplayName("첫 번째 페이지일 경우 start가 1 확인")
    void startPageIs1() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(1)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertThat(noticeList.getStart()).isEqualTo(1);
    }

    @Test
    @DisplayName("첫 번째 페이지일 경우 end가 10 확인")
    void endPageIs10() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(1)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertThat(noticeList.getEnd()).isEqualTo(10);
    }

    @Test
    @DisplayName("전체 페이지가 10의 배수가 아닌 경우 마지막 페이지 확인")
    void totalPageIsNot10() {

        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("")
                .page(11)
                .size(10)
                .build();

        // when
        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        // then
        assertThat(noticeList.getEnd()).isEqualTo(13);
    }


}