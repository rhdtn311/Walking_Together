package backend.server.entity;

import backend.server.exception.ErrorCode;
import backend.server.exception.activityService.ActivityAbnormalDoneWithoutMinimumDistanceException;
import backend.server.exception.activityService.ActivityAbnormalDoneWithoutMinimumTimeException;
import backend.server.exception.activityService.MinimumActivityDistanceNotSatisfyException;
import backend.server.exception.activityService.MinimumActivityTimeNotSatisfyException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;    // 활동번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "std_id")
    private Member member;  // member

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "activity")
    private List<ActivityCheckImages> activityCheckImages = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "activity")
    private List<MapCapture> mapCaptures = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String review;  // 소감문

    private LocalDate activityDate; // 활동 날짜

    private int activityStatus; // 활동 상태

    private LocalDateTime startTime;    // 시작 시간

    private LocalDateTime endTime;   // 종료 시간

    private LocalTime careTime; // 돌봄 활동 환산 시간

    private LocalTime ordinaryTime; // 일반 활동 환산 시간

    private int activityDivision;   // 활동 구분 (돌봄, 일반)

    private Long distance;  // 총 이동거리

    // 종료 시간 입력
    public void changeEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // 거리 입력
    public void changeDistance(Long distance) {
        this.distance = distance;
    }

    // 활동 종료로 변경
    public void changeActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    // 소감문 등록
    public void changeReview(String review) { this.review = review ;}

    // 돌봄 환산 시간
    public void changeCareTime(LocalTime careTime) { this.careTime = careTime;}

    // 일반 환산 시간
    public void changeOrdinaryTime(LocalTime ordinaryTime) {
        this.ordinaryTime = ordinaryTime;
    }

    // MapCapture 저장
    public void setMapCaptures(List<MapCapture> mapCaptures) {
        System.out.println(mapCaptures);
        this.mapCaptures.addAll(mapCaptures);
    }

    // 활동 검증
    public long checkAndSaveActivity(Long distance, int checkNormalQuit, LocalDateTime activityEndTime) {
        this.saveAbnormalActivity(activityEndTime);

        long minutes = ChronoUnit.MINUTES.between(this.startTime, activityEndTime);
        if (minutes < 30) {
            if (checkNormalQuit == 0) {
                return ErrorCode.MINIMUM_ACTIVITY_TIME_NOT_SATISFY.getCode();
            } else if (checkNormalQuit == 1) {
                return ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME.getCode();
            }
        }

        int minimumDistance = this.activityDivision == 0 ? 4000 : 2000;
        if (distance < minimumDistance) {
            if (checkNormalQuit == 0) {
                return ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode();
            } else {
                return ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode();
            }
        }
        return 0;
    }

    public void saveAbnormalActivity(LocalDateTime activityEndTime) {
        this.changeActivityStatus(0);
        this.changeEndTime(activityEndTime);
        this.changeOrdinaryTime(LocalTime.of(0,0));
        this.changeCareTime(LocalTime.of(0,0));
        this.changeDistance(0L);
    }

    // 활동의 환산 시간 변경
    public void changeTotalTime() {
        int totalMinutes = (int) ChronoUnit.MINUTES.between(this.getStartTime(), this.getEndTime());
        int totalHours = (int) ChronoUnit.HOURS.between(this.getStartTime(), this.getEndTime());
        int minutes = (totalMinutes - (60 * totalHours) < 30) ? 0 : 30;

        LocalTime totalTime = LocalTime.of(totalHours, minutes);
        if (this.getActivityDivision() == 0) {
            this.changeOrdinaryTime(totalTime);
        } else {
            this.changeCareTime(totalTime);
        }
    }

    // 활동 중인지 확인
    public boolean isActive() {
        return this.getActivityStatus() != 0;
    }
}
