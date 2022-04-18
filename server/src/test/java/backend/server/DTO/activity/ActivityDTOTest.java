package backend.server.DTO.activity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActivityDTOTest {

    @Test
    @DisplayName("ActivityEndReq로 String map을 저장할 때 정상적으로 HashMap 자료구조로 변환되는지 확인")
    void ActivityEndReqSaveMapToHashMap() {

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

        // DTO에서 입력값으로 받은 String 타입의 map을 Hashmap으로 변환한 mapArrayToHashMap() 메서드 수행 후
        // 반환 값인 Hashmap의 각 키를 뽑아서 나온 값이 내가 생성한 lat, lon, timestamp와 같은지 각각 확인
        HashMap<String, ArrayList<String>> mapCaptures = activityEndReqDTO.mapArrayToHashMap();
        for (int i = 0; i < 10; i++) {
            ArrayList<String> lats = mapCaptures.get("lat");
            ArrayList<String> lons = mapCaptures.get("lon");
            ArrayList<String> timestamps = mapCaptures.get("timestamp");

            assertThat(latList).contains(lats.get(i));
            assertThat(lonList).contains(lons.get(i));
            assertThat(timestampList).contains(timestamps.get(i));
        }
    }

    double makeNumber() {
        return Math.random();
    }

}