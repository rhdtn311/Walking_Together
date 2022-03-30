package backend.server.entity;

import backend.server.DTO.common.CertificationDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Entity
@Table(name = "certification")
public class Certification {

    @Id 
    private Long certificationId; // 활동 아이디와 같음

    private LocalDate activityDate; // 활동일

    private String partnerName; // 파트너 이름

    private String department;  // 학과

    private String name;  // 학생 이름

    private LocalDateTime startTime;  // 활동 시작 시간

    private LocalTime ordinaryTime; // 일반 활동 환산 시간

    private LocalTime careTime; // 돌봄 활동 환산 시간

    private LocalDateTime endTime;  // 종료 시간

    private Long distance;  // 총 이동 거리

    private String stdId;

    private Long activityId;
}
