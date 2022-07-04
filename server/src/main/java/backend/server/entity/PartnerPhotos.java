package backend.server.entity;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
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

    @OneToOne
    @JoinColumn(name = "partnerId")
    private Partner partner;

    public void changeFileUrl(String partnerPhotoUrl) {
        this.partnerPhotoUrl = partnerPhotoUrl;
    }

    public void changeFileName(String partnerPhotoName) {
        this.partnerPhotoName = partnerPhotoName;
    }
}
