package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.entity.Partner;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MapCaptureSaveServiceTest {

    @Autowired
    MapCaptureSaveService mapCaptureSaveService;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    MapCaptureRepository mapCaptureRepository;

    @Test
    @DisplayName("mapCapture의 lat, lon, timestamp가 각각 db에 잘 저장되는지 확인")
    void saveMapCapture() {
        List<String> latList;
        List<String> lonList;
        List<String> timestampList;

        latList = new ArrayList<>();
        lonList = new ArrayList<>();
        timestampList = new ArrayList<>();

        // 무작위로 lat, lon, timestamp를 각각 10개씩 생성 후 각각 검증에 사용할 list에 넣어줌
        for (int i = 0; i < 30; i++) {
            if (i >= 0 && i < 10) {
                String lat = String.valueOf(makeNumber() * 100);
                latList.add(lat);
            } else if (i >= 10 && i < 20) {
                String lon = String.valueOf(makeNumber() * 100);
                lonList.add(lon);
            } else {
                String timestamp = String.valueOf((long) (makeNumber() * 10000000000000L));
                timestampList.add(timestamp);
            }
        }

        // 실제 입력값과 똑같이 위에서 생성한 lat, lon, timestamp를 하나의 String으로 변환
        StringBuilder mapInput = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String lat = latList.get(i);
            mapInput.append("{lat: ").append(lat).append(", ");
            String lon = lonList.get(i);
            mapInput.append("lon: ").append(lon).append(", ");
            String timestamp = timestampList.get(i);
            mapInput.append("timestamp: ").append(timestamp).append("}, ");
        }

        // DTO 생성
        ActivityDTO.ActivityEndReqDTO activityEndReqDTO = ActivityDTO.ActivityEndReqDTO.builder()
                .activityId(1L)
                .endTime("20211111150000")
                .distance(5000L)
                .map(mapInput.toString())
                .checkNormalQuit(0)
                .build();

        // when
        // mapCaptureRepository에 lat, lon, timestamp를 각각 저장
        mapCaptureSaveService.saveMapCapture(activityEndReqDTO.mapArrayToHashMap(), 1L);
        // 방금 저장한 mapCapture를 List형식으로 한 번에 담아서 꺼냄
        List<MapCapture> mapCaptures = mapCaptureRepository.findAllByActivity(Activity.builder().activityId(1L).build());

        // then
        // 각각 기존에 내가 생성한 lat, lon, timestamp에 들어있는지 확인
        for (int i = 0; i < mapCaptures.size(); i++) {
            assertThat(latList).contains(mapCaptures.get(i).getLat());
            assertThat(lonList).contains(mapCaptures.get(i).getLon());
            assertThat(timestampList).contains(mapCaptures.get(i).getTimestamp());
        }
    }

    double makeNumber() {
        return Math.random();
    }

}