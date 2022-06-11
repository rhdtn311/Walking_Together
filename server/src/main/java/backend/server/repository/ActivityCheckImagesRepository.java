package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.ActivityCheckImages;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityCheckImagesRepository extends JpaRepository<ActivityCheckImages,Long>, FileDeleteRepository {

    @Query("select a from ActivityCheckImages a where a.activity.activityId = :activityId")
    Optional<List<ActivityCheckImages>> findImagesByActivityId(@Param("activityId") Long activityId);


    @Override
    default List<ActivityCheckImages> find(FileDeleteDTO fileDeleteDTO) {
        return findImagesByActivityId(fileDeleteDTO.getId()).get();
    }
}
