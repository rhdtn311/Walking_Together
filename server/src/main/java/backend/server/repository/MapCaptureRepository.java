package backend.server.repository;

import backend.server.DTO.admin.MapCaptureDTO;
import backend.server.entity.MapCapture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MapCaptureRepository extends JpaRepository<MapCapture, Long> {

    public List<MapCapture> findAllByActivityId(Long activityId);
}
