package backend.server.repository;

import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapCaptureRepository extends JpaRepository<MapCapture, Long> {

    public List<MapCapture> findAllByActivity(Activity activity);
}
