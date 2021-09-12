package backend.server.repository;

import backend.server.entity.ActivityCheckImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityCheckImagesRepository extends JpaRepository<ActivityCheckImages,Long> {

    Optional<List<ActivityCheckImages>> findActivityCheckImagesByActivityId(Long activityId);
}
