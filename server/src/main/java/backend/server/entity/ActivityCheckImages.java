package backend.server.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
// 활동 시작 사진과 종료 사진을 저장하는 엔티티
@Table(name = "activity_check_images")
public class ActivityCheckImages extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String imageUrl;

    private String imageName;

    // 연관된 활동 Id
    @Column(name = "activity_id")
    private Long activityId;
}
