package backend.server.DTO.feed;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
