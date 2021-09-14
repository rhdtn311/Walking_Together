package backend.server.DTO.admin;

import backend.server.entity.MapCapture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class MapCaptureDTO {

    @NoArgsConstructor
    @Getter
    public static class MapCaptureResDTO {
        private Long mapCaptureId;
        private String lat;
        private String lon;
        private String timestamp;
        private Long activityId;

        @Builder
        public MapCaptureResDTO(Long mapCaptureId, String lat, String lon, String timestamp, Long activityId) {
            this.mapCaptureId = mapCaptureId;
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
            this.activityId = activityId;
        }

        public static List<MapCaptureResDTO> toDTOList(List<MapCapture> mapCaptures) {
            return mapCaptures.stream()
                    .map(MapCapture::toDTO)
                    .collect(Collectors.toList());
        }

    }
}
