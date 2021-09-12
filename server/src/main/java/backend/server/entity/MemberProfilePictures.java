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
@Table(name = "member_profile_pictures")
public class MemberProfilePictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profilePictureId;

    private String profilePictureUrl;

    private String profilePictureName;

    // join 할 학번
    private String stdId;

    public void changeFileUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void changeFileName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }
}
