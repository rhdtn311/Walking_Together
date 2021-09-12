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
@Table(name = "notice_images")
public class NoticeImages {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeImageId;

    private String noticeImageUrl;

    private String noticeImageName;

    private Long noticeId;  // 이미지가 업로드 되어있는 게시물 id

    public void changeImageUrl(String imageUrl) {
        this.noticeImageUrl = imageUrl;
    }

    public void changeImageName(String imageName) {
        this.noticeImageName = imageName;
    }

}
