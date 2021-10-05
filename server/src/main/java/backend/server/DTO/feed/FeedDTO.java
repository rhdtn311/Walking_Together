package backend.server.DTO.feed;

import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.common.MapCaptureDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FeedDTO {
    @Getter
    @NoArgsConstructor
    public static class FeedMainResDTO {

        private Long activityId;
        private int activityStatus;
        private Long distance;
        private LocalDate activityDate;
        private int activityDivision;
        private String partnerName;

        @Builder
        public FeedMainResDTO(Long activityId, int activityStatus, Long distance, LocalDate activityDate,
                              int activityDivision, String partnerName) {
            this.activityId = activityId;
            this.activityStatus = activityStatus;
            this.distance = distance;
            this.activityDate = activityDate;
            this.activityDivision = activityDivision;
            this.partnerName = partnerName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FeedDetailResDTO {
        private LocalDate activityDate;
        private String partnerName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int activityDivision;
        private String review;

        private List<MapCaptureDTO.MapCaptureResDTO> mapPicture;

        @Builder
        public FeedDetailResDTO(LocalDate activityDate, String partnerName, LocalDateTime startTime,
                                LocalDateTime endTime, int activityDivision, String review) {
            this.activityDate = activityDate;
            this.partnerName = partnerName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.activityDivision = activityDivision;
            this.review = review;
        }

        public void setMapPicture(List<MapCaptureDTO.MapCaptureResDTO> mapPicture) {
            this.mapPicture = mapPicture;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class CertificationResDTO {
        List<CertificationDTO> eachCertificationInfos;
        String ordinaryTimes;
        String careTimes;
        String totalTime;

        @Builder
        public CertificationResDTO(List<CertificationDTO> certificationDTOList) {
            this.eachCertificationInfos = certificationDTOList;

            this.ordinaryTimes = getOrdinaryTime(certificationDTOList);
            this.careTimes = getCareTime(certificationDTOList);
            this.totalTime = getTotalTime(certificationDTOList);
        }

        public String getOrdinaryTime(List<CertificationDTO> certificationDTOList) {
            int hours = certificationDTOList.stream().mapToInt(dto -> dto.getOrdinaryTime().getHour()).sum();
            int minutes = certificationDTOList.stream().mapToInt(dto -> dto.getOrdinaryTime().getMinute()).sum();

            return String.format("%02d", (hours + (minutes / 60))) + ":" + String.format("%02d", minutes % 60);
        }

        public String getCareTime(List<CertificationDTO> certificationDTOList) {
            int hours = certificationDTOList.stream().mapToInt(dto -> dto.getCareTime().getHour()).sum();
            int minutes = certificationDTOList.stream().mapToInt(dto -> dto.getCareTime().getMinute()).sum();

            return String.format("%02d", (hours + (minutes / 60))) + ":" + String.format("%02d", minutes % 60);
        }

        public String getTotalTime(List<CertificationDTO> certificationDTOList) {
            int hours = certificationDTOList.stream().mapToInt(dto -> dto.getOrdinaryTime().getHour() + dto.getCareTime().getHour()).sum();
            int minutes = certificationDTOList.stream().mapToInt(dto -> dto.getOrdinaryTime().getMinute() + dto.getCareTime().getMinute()).sum();

            return String.format("%02d", (hours + (minutes / 60))) + ":" + String.format("%02d", minutes % 60);
        }

    }
}
