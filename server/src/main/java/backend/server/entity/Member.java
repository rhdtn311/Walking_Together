package backend.server.entity;

import backend.server.DTO.ranking.RankingDTO;
import backend.server.DTO.myPage.MyPageDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String stdId;   // 학번

    @Column(nullable = false)
    private String name;    // 이름

    @Column(unique = true, nullable = false)
    private String email;   // 이메일

    @Column(nullable = false)
    private String password;    // 비밀번호

    private String birth;   // 생년월일

    private String department;  // 학과

    private String phoneNumber; // 연락처

    private Long distance;    // 총거리

    private int totalTime;   // 총 환산시간

    private String profilePicture;  // 프로필사진 URL

    private boolean activate;   // 활동 상태

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Activity> activities = new ArrayList<>();  // 활동id

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<Partner> partners = new ArrayList<>(); // 파트너 id

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<MemberRole> authorities = new HashSet<>();  // 권한

    public void addMemberRole(MemberRole memberRole) {
        authorities.add(memberRole);
    }

    // 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 학과 변경
    public void changeDepartment(String department) {
        this.department = department;
    }

    // 총 거리 증가
    public void addDistance(Long distance) {
        this.distance += distance;
    }

    // 총 거리 감소
    public void minusDistance(Long distance) {
        this.distance -= distance;

        if (this.distance < 0) {
            this.distance = 0L;
        }
    }

    // 총 환산 시간 수정
    public void changeTotalTime(Activity activity, String cal) {
        int hours = activity.getActivityDivision() == 0 ? activity.getOrdinaryTime().getHour() : activity.getCareTime().getHour();
        int minutes = activity.getActivityDivision() == 0 ? activity.getOrdinaryTime().getMinute() : activity.getCareTime().getMinute();

        int totalMinutes = hours * 60 + minutes;
        if (cal.equals("plus")) {
            this.totalTime += totalMinutes;
        } else {
            this.totalTime -= totalMinutes;
        }

        if (this.totalTime < 0) {
            this.totalTime = 0;
        }
    }
}
