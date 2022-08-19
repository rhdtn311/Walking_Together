package backend.server.DTO.activity;

import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ActivityDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ActivityCreationReqDTO {
        private String stdId;
        private Long partnerId;
        private MultipartFile startPhoto;

        @Builder
        public ActivityCreationReqDTO(String stdId, Long partnerId, MultipartFile startPhoto) {
            this.stdId = stdId;
            this.partnerId = partnerId;
            this.startPhoto = startPhoto;
        }

        public Activity toActivity(Member member, Partner partner) {
            return Activity.builder()
                    .member(member)
                    .partner(partner)
                    .activityDivision(partner.getPartnerDivision())
                    .activityStatus(1)
                    .activityDate(LocalDate.now())
                    .startTime(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ActivityEndReqDTO {

        private Long activityId;
        private Long distance;
        @Nullable
        private String map;
        private String[] mapToArray;
        @Nullable
        private MultipartFile endPhoto;
        private String endTime;
        private LocalDateTime activityEndTime;
        private int checkNormalQuit;

        @Builder
        public ActivityEndReqDTO(Long activityId, Long distance, String map, MultipartFile endPhoto,
                              String endTime, int checkNormalQuit) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            this.activityId = activityId;
            this.distance = distance;
            this.map = map;
            this.endPhoto = endPhoto;
            this.endTime = endTime;
            this.checkNormalQuit = checkNormalQuit;

            if (map != null) {
                this.mapToArray = map.substring(1, map.length() - 1).replace("{", "").replace("}", "")
                        .split(",");
            }
            this.activityEndTime = LocalDateTime.parse(endTime, formatter);
        }

        public HashMap<String, ArrayList<String>> mapArrayToHashMap() {
            HashMap<String, ArrayList<String>> latLonTime = new HashMap<>();
            for (String route : this.getMapToArray()) {
                String[] array = route.split(":");
                String key = array[0].trim();
                String value = array[1].trim();
                latLonTime.computeIfAbsent(key, s -> new ArrayList<String>()).add(value);
            }
            return latLonTime;
        }

        public List<MapCapture> toMapCaptures(Activity activity) {

            List<MapCapture> mapCaptures = new ArrayList<>();
            HashMap<String, ArrayList<String>> map = mapArrayToHashMap();
            int size = map.get("lat").size();
            for (int i = 0; i < size; i++) {
                String lat = map.get("lat").get(i);
                String lon = map.get("lon").get(i);
                String timestamp = map.get("timestamp").get(i);

                mapCaptures.add(MapCapture.builder()
                        .activity(activity)
                        .lat(lat)
                        .lon(lon)
                        .timestamp(timestamp)
                        .build());
            }

            return mapCaptures;
        }
    }
}
