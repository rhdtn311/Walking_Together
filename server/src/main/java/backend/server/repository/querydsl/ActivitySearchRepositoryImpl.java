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
    @Override
    public List<Tuple> activityInfo(String keyword, LocalDate from, LocalDate to, int activityDivision) {

        QActivity activity = QActivity.activity;
        QMember member = QMember.member;
        QPartner partner = QPartner.partner;

        JPQLQuery<Activity> jpqlQuery = from(activity);
        jpqlQuery.leftJoin(member).on(member.eq(activity.member));
        jpqlQuery.leftJoin(partner).on(partner.eq(activity.partner));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(member.name, member.department, member.stdId, activity.activityDate,
                activity.startTime, activity.activityId, activity.endTime, activity.distance, partner.partnerName);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = activity.activityId.isNotNull();

        booleanBuilder.and(expression);

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(keyword != null) {
            conditionBuilder.and(member.stdId.contains(keyword));
            conditionBuilder.or(member.name.contains(keyword));

            booleanBuilder.and(conditionBuilder);
        }

        if(from != null && to != null) {

            conditionBuilder.and(activity.activityDate.between(from, to));

            booleanBuilder.and(conditionBuilder);
        }

        if (activityDivision == 0) {
            conditionBuilder.and(activity.activityDivision.eq(0));
            booleanBuilder.and(conditionBuilder);
        } else if (activityDivision == 1) {
            conditionBuilder.and(activity.activityDivision.eq(1));
            booleanBuilder.and(conditionBuilder);
        } else if (activityDivision == 2) {
            conditionBuilder.or(activity.activityDivision.eq(0));
            conditionBuilder.or(activity.activityDivision.eq(1));
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);
        tuple.orderBy(member.name.asc());

        return tuple.fetch();

    }

    // 활동 상세 조회 ( 나중에 )
    @Override
    public List<Tuple> activityDetail(Long activityId) {

        QActivity activity = QActivity.activity;
        QMember member = QMember.member;
        QPartner partner = QPartner.partner;

        JPQLQuery<Activity> jpqlQuery = from(activity);

        jpqlQuery.leftJoin(member).on(activity.member.eq(member));
        jpqlQuery.leftJoin(partner).on(activity.partner.eq(partner));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(member.name, member.department, member.stdId, activity.activityDate,
                partner.partnerName, activity.review, activity.distance, activity.startTime, activity.endTime);

        tuple.where(activity.activityId.eq(activityId));

        List<Tuple> result = tuple.fetch();

        return result;
    }

    // 피드 메인
    @Override
    public List<Tuple> feed(String stdId, String sort) {

        QMember member = QMember.member;
        QActivity activity = QActivity.activity;
        QPartner partner = QPartner.partner;

        JPQLQuery<Activity> jpqlQuery = from(activity);
        jpqlQuery.leftJoin(member).on(member.eq(activity.member));
        jpqlQuery.leftJoin(partner).on(partner.eq(activity.partner));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(activity.activityStatus, activity.distance, partner.partnerName,
                activity.activityDate,activity.activityDivision, activity.activityId);

        tuple.where(activity.activityId.gt(0L));
        tuple.where(activity.member.stdId.eq(stdId));

        tuple.orderBy(activity.activityStatus.desc());

        if (sort.equals("desc")) {
            tuple.orderBy(activity.activityDate.desc());
        } else if (sort.equals("asc")) {
            tuple.orderBy(activity.activityDate.asc());
        }

        List<Tuple> result = tuple.fetch();

        return result;
    }

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
