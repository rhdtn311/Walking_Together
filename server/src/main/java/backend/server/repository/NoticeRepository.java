package backend.server.repository;

import backend.server.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>,QuerydslPredicateExecutor<Notice> {

    Notice findNoticeByNoticeId(Long noticeId);

}
