package backend.server.repository.querydsl;

import backend.server.entity.Activity;
import backend.server.entity.QActivity;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static backend.server.entity.QActivity.activity;
import static backend.server.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class ActivityQueryRepository {
    private final JPQLQueryFactory jpqlQueryFactory;

    public boolean existsActiveActivity(String stdId) {
        Activity findActivity = jpqlQueryFactory.select(QActivity.activity)
                .from(QActivity.activity)
                .where(activity.member.stdId.eq(stdId))
                .where(activity.activityStatus.eq(1))
                .fetchFirst();

        return findActivity != null;
    }
}

