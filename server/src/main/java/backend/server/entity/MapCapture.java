package backend.server.entity;

import backend.server.DTO.admin.MapCaptureDTO;
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
// 활동 후 경로 캡쳐
@Table(name = "map_capture")
public class MapCapture {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapCaptureId;

    private String lat;

    private String lon;

    private String timestamp;

    // join 할 활동 id
    private Long activityId;

    public MapCaptureDTO.MapCaptureResDTO toDTO() {
        return MapCaptureDTO.MapCaptureResDTO.builder()
                .activityId(this.activityId)
                .mapCaptureId(this.mapCaptureId)
                .lat(this.lat)
                .lon(this.lon)
                .timestamp(this.timestamp)
                .build();
    }
}
