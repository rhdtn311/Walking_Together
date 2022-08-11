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
@Table(name = "partnerPhoto")
public class PartnerPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerPhotoId;

    private String partnerPhotoUrl;

    private String partnerPhotoName;

    public void changeFileUrl(String partnerPhotoUrl) {
        this.partnerPhotoUrl = partnerPhotoUrl;
    }

    public void changeFileName(String partnerPhotoName) {
        this.partnerPhotoName = partnerPhotoName;
    }
}
