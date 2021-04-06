package backend.server.DTO.feed;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedDetailDTO {

    private LocalDate activityDate;

    private String partnerName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer activityDivision;

    private String review;

    private String mapPicture;
}
