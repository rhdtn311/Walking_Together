package backend.server.DTO.ranking;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingDTO {

    private String name;
    private String department;
    private String stdId;
    private Long totalDistance;
}
