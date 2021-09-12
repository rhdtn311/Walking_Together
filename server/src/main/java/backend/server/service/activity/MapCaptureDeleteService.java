package backend.server.service.activity;

import backend.server.entity.MapCapture;
import backend.server.repository.MapCaptureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class MapCaptureDeleteService {

    private final MapCaptureRepository mapCaptureRepository;

    public long deleteMapCaptures(long activityId) {
        ArrayList<Object> allMapsByActivity = mapCaptureRepository.findAllByActivityId(activityId);

        if (!allMapsByActivity.isEmpty()) {
            for (Object obj : allMapsByActivity) {
                mapCaptureRepository.delete((MapCapture) obj);
            }
        }
        return activityId;
    }
}
