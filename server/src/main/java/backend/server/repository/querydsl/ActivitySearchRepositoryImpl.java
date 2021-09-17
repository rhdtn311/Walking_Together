package backend.server.repository.querydsl;

import backend.server.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivitySearchRepositoryImpl extends QuerydslRepositorySupport implements ActivitySearchRepository {

    public ActivitySearchRepositoryImpl() {
        super(Activity.class);
    }

    // 관리자 - 활동조회

    // 활동 상세 조회 ( 나중에 )

    // 피드 메인


    // 피드 상세
    @Override
    public Tuple feedDetail(Long activityId) {
        QMember member = QMember.member;
        QActivity activity = QActivity.activity;
        QPartner partner = QPartner.partner;
        QMapCapture mapCapture = QMapCapture.mapCapture;

        JPQLQuery<Activity> jpqlQuery = from(activity);
        jpqlQuery.leftJoin(member).on(member.eq(activity.member));
        jpqlQuery.leftJoin(partner).on(partner.eq(activity.partner));
        jpqlQuery.leftJoin(mapCapture).on(activity.activityId.eq(mapCapture.activityId));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(activity.activityDate, activity.partner.partnerName, activity.startTime, activity.endTime,
                activity.activityDivision, activity.review);

        tuple.where(activity.activityId.eq(activityId));

        return tuple.fetch().get(0);
    }

    // 진행 중인 활동이 있는지 확인
    @Override
    public boolean findDoingActivity(String stdId) {
        QMember member = QMember.member;
        QActivity activity = QActivity.activity;

        JPQLQuery<Activity> jpqlQuery = from(activity);
        jpqlQuery.leftJoin(member).on(member.stdId.eq(activity.member.stdId));

        jpqlQuery.select(activity);

        jpqlQuery.where(member.stdId.eq(stdId));
        jpqlQuery.where(activity.activityStatus.eq(1));

        List<Activity> result = jpqlQuery.fetch();

        // 활동중인 활동이 없다면 false를 반환 있다면 true 반환
        if (result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
