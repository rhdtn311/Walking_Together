package backend.server.DTO.ranking;

import backend.server.entity.Member;
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

    public static RankingDTO entityToDto(Member member) {
        return RankingDTO.builder()
                .stdId(member.getStdId())
                .name(member.getName())
                .department(member.getDepartment())
                .totalDistance(member.getDistance())
                .build();
    }
}
