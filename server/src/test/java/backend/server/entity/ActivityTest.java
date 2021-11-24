package backend.server.entity;

import backend.server.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    @DisplayName("활동 시간이 30분 미만이고, 정상 종료일 때")
    void activityTimeUnder30MinutesAndNormalQuit() {
        // given
        Activity under30MinutesActivity = Activity.builder()
                .activityId(1L)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over30MinutesActivity = Activity.builder()
                .activityId(2L)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();


        // when
        // 활동 종료 시간 - 활동 시작 시간 = 29분
        LocalDateTime under30MinutesActivityEndTime =
                LocalDateTime.of(2021, 11, 24, 10, 29);

        // 활동 종료 시간 - 활동 시작 시간 = 30분
        LocalDateTime over30MinutesActivityEndtime =
                LocalDateTime.of(2021, 11, 24, 10, 30);


        // then
        long errorResult = under30MinutesActivity.checkAndSaveActivity(5000L, 0, under30MinutesActivityEndTime);
        long correctResult = over30MinutesActivity.checkAndSaveActivity(5000L, 0, over30MinutesActivityEndtime);

        assertThat(errorResult).isEqualTo(ErrorCode.MINIMUM_ACTIVITY_TIME_NOT_SATISFY.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.MINIMUM_ACTIVITY_TIME_NOT_SATISFY.getCode());
    }

    @Test
    @DisplayName("활동 시간이 30분 미만이고, 비정상 종료일 때")
    void activityTimeUnder30MinutesAndAbnormalQuit() {
        // given
        Activity under30MinutesActivity = Activity.builder()
                .activityId(1L)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over30MinutesActivity = Activity.builder()
                .activityId(2L)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        // when
        // 활동 종료 시간 - 활동 시작 시간 = 29분
        LocalDateTime under30MinutesActivityEndTime =
                LocalDateTime.of(2021, 11, 24, 10, 29);

        // 활동 종료 시간 - 활동 시작 시간 = 30분
        LocalDateTime over30MinutesActivityEndtime =
                LocalDateTime.of(2021, 11, 24, 10, 30);

        // then
        long errorResult = under30MinutesActivity.checkAndSaveActivity(5000L, 1, under30MinutesActivityEndTime);
        long correctResult = over30MinutesActivity.checkAndSaveActivity(5000L, 1, over30MinutesActivityEndtime);

        assertThat(errorResult).isEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME.getCode());
    }

    @Test
    @DisplayName("활동이 일반 활동이고, 활동 거리가 4000m 미만이고, 정상 종료일 때")
    void ordinaryActivityUnder4000mAndNormalQuit() {
        // given
        Activity under4000mActivity = Activity.builder()
                .activityId(1L)
                .activityDivision(0)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over4000mActivity = Activity.builder()
                .activityId(2L)
                .activityDivision(0)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        // when
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 24, 10, 30);

        long errorResult = under4000mActivity.checkAndSaveActivity(3999L, 0, endTime);
        long correctResult = over4000mActivity.checkAndSaveActivity(4000L, 0, endTime);

        // then
        assertThat(errorResult).isEqualTo(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode());
    }

    @Test
    @DisplayName("활동이 일반 활동이고, 활동 거리가 4000m 미만이고, 비정상 종료일 때")
    void ordinaryActivityUnder4000mAndAbnormalQuit() {
        // given
        Activity under4000mActivity = Activity.builder()
                .activityId(1L)
                .activityDivision(0)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over4000mActivity = Activity.builder()
                .activityId(2L)
                .activityDivision(0)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        // when
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 24, 10, 30);

        long errorResult = under4000mActivity.checkAndSaveActivity(3999L, 1, endTime);
        long correctResult = over4000mActivity.checkAndSaveActivity(4000L, 1, endTime);

        // then
        assertThat(errorResult).isEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode());
    }

    @Test
    @DisplayName("활동이 돌봄 활동이고, 활동 거리가 2000m 미만이고, 정상 종료일 때")
    void careActivityUnder2000mAndNormalQuit() {
        // given
        Activity under2000mActivity = Activity.builder()
                .activityId(1L)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over2000mActivity = Activity.builder()
                .activityId(2L)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        // when
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 24, 10, 30);

        long errorResult = under2000mActivity.checkAndSaveActivity(1999L, 0, endTime);
        long correctResult = over2000mActivity.checkAndSaveActivity(2000L, 0, endTime);

        // then
        assertThat(errorResult).isEqualTo(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY.getCode());
    }

    @Test
    @DisplayName("활동이 돌봄 활동이고, 활동 거리가 2000m 미만이고, 비정상 종료일 때")
    void careActivityUnder2000mAndAbnormalQuit() {

        // given
        Activity under2000mActivity = Activity.builder()
                .activityId(1L)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        Activity over2000mActivity = Activity.builder()
                .activityId(2L)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 11, 24, 10, 00))
                .build();

        // when
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 24, 10, 30);

        long errorResult = under2000mActivity.checkAndSaveActivity(1999L, 1, endTime);
        long correctResult = over2000mActivity.checkAndSaveActivity(2000L, 1, endTime);

        // then
        assertThat(errorResult).isEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode());
        assertThat(correctResult).isNotEqualTo(ErrorCode.ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE.getCode());
    }}