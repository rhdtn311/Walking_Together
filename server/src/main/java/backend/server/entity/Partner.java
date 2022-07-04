package backend.server.entity;

import backend.server.DTO.activity.PartnerDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId; // 파트너 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "std_id")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "partner")
    private List<Activity> activity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnerPhotoId")
    private PartnerPhotos partnerPhoto;

    private String partnerName; // 파트너 이름

    private String partnerBirth;    // 파트너 출생일

    private int partnerDivision; // 파트너 구분

    private String partnerDetail;   // 파트너 세부 정보(임산부, 장애인, 아동, 일반인)

    private String gender;  // 파트너 성별

    private String selectionReason; // 선정이유

    private String relationship;    // 관계

    public void changePartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void changePartnerDetail(String partnerDetail) {
        this.partnerDetail = partnerDetail;

        if (partnerDetail.equals("o")) {
            this.partnerDivision = 0;
        } else {
            this.partnerDivision = 1;
        }
    }

    public void changePartnerSelectionReason(String selectionReason) {
        this.selectionReason = selectionReason;
    }

    public void changePartnerRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void changePartnerGender(String gender) {
        this.gender = gender;
    }

    public void changePartnerBirth(String partnerBirth) {
        this.partnerBirth = partnerBirth;
    }

}