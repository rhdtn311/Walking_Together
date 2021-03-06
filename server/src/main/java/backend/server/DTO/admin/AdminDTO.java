package backend.server.DTO.admin;

import backend.server.DTO.common.MapCaptureDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDTO {

    @Getter
    @NoArgsConstructor
    public static class MemberInfoResDTO {
        private String name;
        private String stdId;
        private String department;
        private String email;
        private String birth;
        private String phoneNumber;

        @Builder
        public MemberInfoResDTO(String name, String stdId, String department, String email, String birth, String phoneNumber) {
            this.name = name;
            this.stdId = stdId;
            this.department = department;
            this.email = email;
            this.birth = birth;
            this.phoneNumber = phoneNumber;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ActivityInfoReqDTO {
        private String keyword;
        private String from;
        private String to;
        private int activityDivision;
        private LocalDate fromDate;
        private LocalDate toDate;

        @Builder
        public ActivityInfoReqDTO(String keyword, String from, String to, int activityDivision) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            this.keyword = keyword;
            this.from = from;
            this.to = to;
            this.activityDivision = activityDivision;
            this.fromDate = LocalDate.parse(from, formatter);
            this.toDate = LocalDate.parse(to, formatter);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ActivityInfoResDTO {

        private Long activityId;
        private String stdId;
        private String partnerName;
        private String stdName;
        private String department;
        private String activityDate;
        private String startTime;
        private String endTime;
        private Long totalDistance;

        @Builder
        public ActivityInfoResDTO(Long activityId, String stdId, String partnerName, String stdName,
                                  String department, LocalDate activityDate, LocalDateTime startTime,
                                  LocalDateTime endTime, Long totalDistance) {
            this.activityId = activityId;
            this.stdId = stdId;
            this.partnerName = partnerName;
            this.stdName = stdName;
            this.department = department;
            this.activityDate = activityDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.startTime = startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            this.endTime = endTime == null ? null : endTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            this.totalDistance = totalDistance;
        }
    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class ActivityDetailInfoResDTO {
        private String stdName;
        private String department;
        private String stdId;
        private String partnerName;
        private String review;
        private LocalDate activityDate;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long totalDistance;

        private List<MapCaptureDTO.MapCaptureResDTO> mapPicture;
        private LocalTime totalTime;

        @Builder
        public ActivityDetailInfoResDTO(String stdName, String department, String stdId, String partnerName,
                                        String review, LocalDate activityDate, LocalDateTime startTime,
                                        LocalDateTime endTime, Long totalDistance) {
            this.stdName = stdName;
            this.department = department;
            this.stdId = stdId;
            this.partnerName = partnerName;
            this.review = review;
            this.activityDate = activityDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalDistance = totalDistance;
        }

        public void setTotalTime(LocalTime totalTime) {
            this.totalTime = totalTime;
        }

        public void setMapPicture(List<MapCaptureDTO.MapCaptureResDTO> mapCaptureResDTOS) {
            this.mapPicture = mapCaptureResDTOS;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PartnerInfoResDTO {
        private String stdId;
        private String stdName;
        private String department;
        private String partnerName;
        private String partnerBirth;
        private String gender;
        private String relation;
        private int partnerDivision;

        @Builder
        public PartnerInfoResDTO(String stdId, String stdName, String department, String partnerName,
                                 String partnerBirth, String gender, String relation, int partnerDivision) {
            this.stdId = stdId;
            this.stdName = stdName;
            this.partnerName = partnerName;
            this.department = department;
            this.partnerBirth = partnerBirth;
            this.gender = gender;
            this.relation = relation;
            this.partnerDivision = partnerDivision;
        }
    }
}
