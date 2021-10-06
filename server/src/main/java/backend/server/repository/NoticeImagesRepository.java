package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.NoticeImages;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeImagesRepository extends JpaRepository<NoticeImages,Long>, FileDeleteRepository {

    public List<NoticeImages> findNoticeImagesByNoticeId(Long noticeId);

    boolean existsNoticeImagesByNoticeId(Long noticeId);

    NoticeImages findFirstByNoticeId(Long noticeId);

    @Override
    default NoticeImages find(FileDeleteDTO fileDeleteDTO) {
        return findFirstByNoticeId(fileDeleteDTO.getId());
    }
}
