package backend.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "partner_photos")
public class PartnerPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerPhotoId;

    private String partnerPhotoUrl;

    private String partnerPhotoName;

    // join 할 파트너Id
    private Long partnerId;
}
