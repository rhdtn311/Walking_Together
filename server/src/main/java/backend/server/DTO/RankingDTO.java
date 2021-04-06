package backend.server.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {

    private String name;

    private String department;

    private String stdId;

    private Long totalDistance;
}
