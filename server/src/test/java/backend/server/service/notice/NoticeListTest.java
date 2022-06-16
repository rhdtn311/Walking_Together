package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.entity.Notice;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class NoticeListTest {

    @Autowired
    NoticeListService noticeListService;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeQueryRepository noticeQueryRepository;

    @Test
    public void init() {

        for (int i = 1; i <= 100; i++) {
            Notice notice = Notice.builder()
                    .title("notice title " + i)
                    .content("notice content " + i)
                    .build();

            noticeRepository.save(notice);
        }
    }

    @Test
    public void test() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2)
                .size(5)
                .keyword("")
                .build();

        PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> noticeList = noticeListService.findNotices(pageRequestDTO);

        System.out.println(noticeList);
        System.out.println(noticeList.getPageDataList());
    }

    @Test
    public void test1() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("")
                .build();

        Pageable page = pageRequestDTO.getPageable(Sort.by("noticeId").descending());
        BooleanBuilder booleanBuilder = noticeQueryRepository.getNoticeSearch(pageRequestDTO.getKeyword());

        Page<Notice> result = noticeRepository.findAll(booleanBuilder, page);

        for (Notice notice : result) {
            System.out.println(notice.getTitle());
        }

    }
}
