package backend.server.DTO.common;

import backend.server.entity.Certification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CertificationDTO {
        private Long certificationId;
        private LocalDate activityDate;
        private String partnerName;
        private String department;
        private String name;
        private LocalDateTime startTime;
        private LocalTime ordinaryTime;
        private LocalTime careTime;
        private LocalDateTime endTime;
        private Long distance;
        private String stdId;

        @Builder
        public CertificationDTO (Long certificationId, LocalDate activityDate, String partnerName,
                                String department, String name, LocalDateTime startTime,
                                LocalTime ordinaryTime, LocalTime careTime, LocalDateTime endTime,
                                Long distance, String stdId, Long activityId) {
            this.certificationId = certificationId;
            this.activityDate = activityDate;
            this.partnerName = partnerName;
            this.department = department;
            this.name = name;
            this.startTime = startTime;
            this.ordinaryTime = ordinaryTime;
            this.careTime = careTime;
            this.endTime = endTime;
            this.distance = distance;
            this.stdId = stdId;
    }

    public static List<CertificationDTO> toDTOList(List<Certification> certificationList) {
            return certificationList.stream()
                    .map(Certification::toDTO)
                    .collect(Collectors.toList());
    }
}
