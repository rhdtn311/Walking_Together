package backend.server.repository.querydsl;

import backend.server.DTO.admin.AdminDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import static backend.server.entity.QMember.member;
import static backend.server.entity.QActivity.activity;
import static backend.server.entity.QPartner.partner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class AdminQueryRepository {

    private final JPQLQueryFactory queryFactory;

    public List<AdminDTO.MemberInfoResDTO> findMemberInfo(String keyword) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(eqName(keyword)).or(eqStdId(keyword));

        return queryFactory.select(Projections.constructor(AdminDTO.MemberInfoResDTO.class,
                member.name, member.stdId, member.department, member.email, member.birth, member.phoneNumber))
                .from(member)
                .where(booleanBuilder)
                .fetch();
    }

    public BooleanExpression eqName(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return member.name.contains(keyword);
    }

    public BooleanExpression eqStdId(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return member.stdId.contains(keyword);
    }

    public List<AdminDTO.ActivityInfoResDTO> findActivityInfo(AdminDTO.ActivityInfoReqDTO activityInfoReqDTO) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder
                .or(eqName(activityInfoReqDTO.getKeyword()))
                .or(eqStdId(activityInfoReqDTO.getKeyword()))
                .and(betweenDate(activityInfoReqDTO.getFromDate(), activityInfoReqDTO.getToDate()))
                .and(eqActivityDivision(activityInfoReqDTO.getActivityDivision()));

        return  queryFactory.select(Projections.constructor(AdminDTO.ActivityInfoQueryDTO.class,
                activity.activityId, member.stdId, partner.partnerName, member.name, member.department,
                activity.activityDate, activity.startTime, activity.endTime, activity.distance))
                .from(activity)
                .leftJoin(member).on(member.eq(activity.member))
                .leftJoin(partner).on(partner.eq(activity.partner))
                .where(booleanBuilder)
                .orderBy(member.name.asc())
                .fetch()
                .stream().map(AdminDTO.ActivityInfoQueryDTO::toActivityInfoResDTO)
                .collect(Collectors.toList());
    }

    public BooleanExpression eqActivityDivision(int activityDivision) {

        return activityDivision != 2 ? activity.activityDivision.eq(activityDivision) : activity.isNotNull();
    }

    public BooleanExpression betweenDate(LocalDate from, LocalDate to) {

        return activity.activityDate.between(from, to);
    }
}
