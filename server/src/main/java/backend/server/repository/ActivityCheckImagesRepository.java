package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.ActivityCheckImages;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityCheckImagesRepository extends JpaRepository<ActivityCheckImages,Long>, FileDeleteRepository {

    Optional<List<ActivityCheckImages>> findActivityCheckImagesByActivityId(Long activityId);

    @Override
    default List<ActivityCheckImages> find(FileDeleteDTO fileDeleteDTO) {
        return findActivityCheckImagesByActivityId(fileDeleteDTO.getId()).get();
    }
}
