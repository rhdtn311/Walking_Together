package backend.server.entity;

import backend.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("회원의 총 거리 감소")
    void memberTotalDistanceWhenActivityDelete() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .name("name")
                .password("password")
                .distance(5000L)
                .email("email.com")
                .build();

        Long memberOriginalDistance = member.getDistance();

        // when
        Long minusDistance = 500L;
        member.minusDistance(minusDistance);

        // then
        assertThat(member.getDistance()).isEqualTo(memberOriginalDistance - minusDistance);
    }

    @Test
    @DisplayName("회원의 총 거리 증가, 감소 : 총 거리가 0보다 낮을 시 0으로 바뀌는지 확인")
    void memberTotalDistanceUnder0() {

        // given
        Member member = Member.builder()
                .stdId("stdId")
                .name("name")
                .password("password")
                .distance(5000L)
                .email("email.com")
                .build();

        // when
        member.minusDistance(member.getDistance() + 1L);

        // then
        assertThat(member.getDistance()).isEqualTo(0L);
    }

    @Test
    @DisplayName("회원의 총 시간 증가, 감소")
    void memberTotalTimeWhenActivityDelete() {

        // given
        Member member1 = Member.builder()
                .stdId("stdId1")
                .name("name1")
                .password("password1")
                .email("email1.com")
                .distance(1000L)
                .totalTime(1000)
                .build();

        int member1OriginalTotalTime = member1.getTotalTime();

        Member member2 = Member.builder()
                .stdId("stdId2")
                .name("name2")
                .password("password2")
                .email("email2.com")
                .distance(1000L)
                .totalTime(1000)
                .build();

        int member2OriginalTotalTime = member2.getTotalTime();

        Member member3 = Member.builder()
                .stdId("stdId3")
                .name("name3")
                .password("password3")
                .email("email3.com")
                .distance(1000L)
                .totalTime(29)
                .build();

        Activity ordinaryActivity = Activity.builder()
                .member(member1)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(5,30))   // 330
                .build();

        Activity careActivity = Activity.builder()
                .member(member2)
                .activityDivision(1)
                .careTime(LocalTime.of(2,12)) // 132
                .build();

        Activity activity = Activity.builder()
                .member(member3)
                .activityDivision(0)
                .ordinaryTime(LocalTime.of(0,30)) // 132
                .build();

        // when
        member1.changeTotalTime(ordinaryActivity, "minus");
        member2.changeTotalTime(careActivity, "plus");
        member3.changeTotalTime(activity, "minus");

        // then
        assertThat(member1.getTotalTime()).isEqualTo(member1OriginalTotalTime - 330);
        assertThat(member2.getTotalTime()).isEqualTo(member2OriginalTotalTime + 132);
        assertThat(member3.getTotalTime()).isEqualTo(0L);
    }
}