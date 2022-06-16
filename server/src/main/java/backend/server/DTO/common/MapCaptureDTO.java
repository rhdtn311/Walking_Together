package backend.server.DTO.common;

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

        public static MapCaptureResDTO entityToDto(MapCapture mapCapture) {
            return MapCaptureResDTO.builder()
                    .activityId(mapCapture.getActivity().getActivityId())
                    .mapCaptureId(mapCapture.getMapCaptureId())
                    .lat(mapCapture.getLat())
                    .lon(mapCapture.getLon())
                    .timestamp(mapCapture.getTimestamp())
                    .build();
        }

        public static List<MapCaptureResDTO> toDTOList(List<MapCapture> mapCaptures) {
            return mapCaptures.stream()
                    .map(MapCaptureResDTO::entityToDto)
                    .collect(Collectors.toList());
        }

    }
}
