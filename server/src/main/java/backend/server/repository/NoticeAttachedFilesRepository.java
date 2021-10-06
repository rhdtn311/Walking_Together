package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachedFilesRepository extends JpaRepository<NoticeAttachedFiles, Long>, FileDeleteRepository {

    public List<NoticeAttachedFiles> findNoticeAttachedFilesByNoticeId(Long noticeId);

    boolean existsNoticeAttachedFilesByNoticeId(Long noticeId);

    NoticeAttachedFiles findFirstByNoticeId(Long noticeId);

    @Override
    default NoticeAttachedFiles find(FileDeleteDTO fileDeleteDTO) {
        return findFirstByNoticeId(fileDeleteDTO.getId());
    }
}
