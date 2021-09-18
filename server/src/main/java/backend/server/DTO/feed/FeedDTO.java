package backend.server.DTO.feed;

import backend.server.DTO.common.MapCaptureDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedDTO {

    private int activityStatus;

    private Long distance;

    private String partnerName;

    private LocalDate activityDate;

    private int activityDivision;

    private Long activityId;

    @Getter
    @NoArgsConstructor
    public static class FeedMainResDTO {

        private Long activityId;
        private int activityStatus;
        private Long distance;
        private LocalDate activityDate;
        private int activityDivision;
        private String partnerName;

        @Builder
        public FeedMainResDTO(Long activityId, int activityStatus, Long distance, LocalDate activityDate,
                              int activityDivision, String partnerName) {
            this.activityId = activityId;
            this.activityStatus = activityStatus;
            this.distance = distance;
            this.activityDate = activityDate;
            this.activityDivision = activityDivision;
            this.partnerName = partnerName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FeedDetailResDTO {
        private LocalDate activityDate;
        private String partnerName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int activityDivision;
        private String review;

        private List<MapCaptureDTO.MapCaptureResDTO> mapPicture;

        @Builder
        public FeedDetailResDTO(LocalDate activityDate, String partnerName, LocalDateTime startTime,
                                LocalDateTime endTime, int activityDivision, String review) {
            this.activityDate = activityDate;
            this.partnerName = partnerName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.activityDivision = activityDivision;
            this.review = review;
        }

        public void setMapPicture(List<MapCaptureDTO.MapCaptureResDTO> mapPicture) {
            this.mapPicture = mapPicture;
        }
    }
}
