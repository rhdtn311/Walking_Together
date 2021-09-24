package backend.server.DTO.myPage;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 파트너 리스트 출력 DTO
public class MyPagePartnerDTO {

    private String partnerName; // 파트너 이름
    private String partnerDetail;   // 파트너 세부정
    private String partnerBirth;    // 파트너 생년월일
    private Long partnerId; // 파트너 아이디
    private String gender;  // 파트너 성별
    private String selectionReason; // 파트너 선정 이유
    private String relationship;    // 파트너와의 관계
    private String stdId;   // 학번
}
