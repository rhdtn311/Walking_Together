package backend.server.entity;

import backend.server.DTO.common.MapCaptureDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// 활동 후 경로 캡쳐
@Table(name = "map_capture")
public class MapCapture {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapCaptureId;

    private String lat;

    private String lon;

    private String timestamp;

    // join 할 활동 id
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
