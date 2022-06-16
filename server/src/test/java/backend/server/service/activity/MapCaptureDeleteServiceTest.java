package backend.server.service.activity;

import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static backend.server.entity.QMapCapture.mapCapture;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MapCaptureDeleteServiceTest {

    @Autowired
    MapCaptureDeleteService mapCaptureDeleteService;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    MapCaptureRepository mapCaptureRepository;

    @Test
    @DisplayName("[삭제]저장한 MapCaptures가 제대로 삭제되는지 확인")
    void mapCapturesDelete() {
//
//        // given
//        for (int i = 0; i < 3; i++) {
//            String lat = String.valueOf(makeNumber() * 100);
//            String lon = String.valueOf(makeNumber() * 100);
//            String timestamp = String.valueOf((long) (makeNumber() * 10000000000000L));
//
//            mapCaptureRepository.save(MapCapture.builder()
//                    .lat(lat)
//                    .lon(lon)
//                    .timestamp(timestamp)
//                    .activity(Activity.builder().activityId(1L).build()).build());
//        }

        // when
//        mapCaptureDeleteService.deleteMapCaptures(1L);

        // then
//        assertThat(mapCaptureRepository.findAllByActivityId(1L)).isEmpty();
    }

    double makeNumber() {
        return Math.random();
    }

}