package backend.server.service.activity;

import backend.server.entity.MapCapture;
import backend.server.repository.MapCaptureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class MapCaptureSaveService {

    private final MapCaptureRepository mapCaptureRepository;

    public void saveMapCapture(HashMap<String, ArrayList<String>> mapToArrayToHashMap, Long activityId) {

        int size = mapToArrayToHashMap.get("lat").size();
        for (int i = 0; i < size; i++) {
            String lat = mapToArrayToHashMap.get("lat").get(i);
            String lon = mapToArrayToHashMap.get("lon").get(i);
            String timestamp = mapToArrayToHashMap.get("timestamp").get(i);

            mapCaptureRepository.save(
                    MapCapture.builder().activityId(activityId).lat(lat).lon(lon).timestamp(timestamp).build());
        }
    }
}
