package backend.server.DTO.admin;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ActivityInfoQueryDTO {

        private Long activityId;
        private String stdId;
        private String partnerName;
        private String stdName;
        private String department;
        private LocalDate activityDate;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long totalDistance;

        @Builder
        public ActivityInfoQueryDTO(Long activityId, String stdId, String partnerName, String stdName,
                                  String department, LocalDate activityDate, LocalDateTime startTime,
                                  LocalDateTime endTime, Long totalDistance) {
            this.activityId = activityId;
            this.stdId = stdId;
            this.partnerName = partnerName;
            this.stdName = stdName;
            this.department = department;
            this.activityDate = activityDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalDistance = totalDistance;
        }

        public ActivityInfoResDTO toActivityInfoResDTO() {
            return ActivityInfoResDTO.builder()
                    .activityId(this.activityId)
                    .stdId(this.stdId)
                    .partnerName(this.partnerName)
                    .stdName(this.stdName)
                    .department(this.department)
                    .activityDate(this.activityDate)
                    .startTime(this.startTime)
                    .endTime(this.endTime)
                    .totalDistance(this.totalDistance)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
