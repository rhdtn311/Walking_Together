package backend.server.repository;

import backend.server.entity.NoticeImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeImagesRepository extends JpaRepository<NoticeImages,Long> {

    public List<NoticeImages> findNoticeImagesByNoticeId(Long noticeId);

    boolean existsNoticeImagesByNoticeId(Long noticeId);

    NoticeImages findFirstByNoticeId(Long noticeId);
}
