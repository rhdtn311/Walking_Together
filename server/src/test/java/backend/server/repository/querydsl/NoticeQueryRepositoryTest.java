package backend.server.repository.querydsl;

import backend.server.DTO.notice.NoticeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoticeQueryRepositoryTest {

    @Autowired
    NoticeQueryRepository noticeQueryRepository;

    @Transactional
    @Test
    public void test() {

    }


}