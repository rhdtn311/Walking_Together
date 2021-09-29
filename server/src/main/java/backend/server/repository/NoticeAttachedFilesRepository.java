package backend.server.repository;

import backend.server.entity.NoticeAttachedFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachedFilesRepository extends JpaRepository<NoticeAttachedFiles, Long> {

    public List<NoticeAttachedFiles> findNoticeAttachedFilesByNoticeId(Long noticeId);

    boolean existsNoticeAttachedFilesByNoticeId(Long noticeId);

    NoticeAttachedFiles findFirstByNoticeId(Long noticeId);
}
