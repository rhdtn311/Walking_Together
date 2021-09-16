package backend.server.repository.querydsl;

import backend.server.DTO.admin.AdminDTO;
import backend.server.DTO.admin.MapCaptureDTO;
import backend.server.entity.*;
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
import static backend.server.entity.QMapCapture.mapCapture;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class AdminQueryRepository {

    private final JPQLQueryFactory queryFactory;

    // 회원 정보 조회
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

    // 활동 정보 조회
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

    // 활동 세부 정보 조회
    public AdminDTO.ActivityDetailInfoResDTO findActivityDetailInfo(Long activityId) {

        AdminDTO.ActivityDetailInfoResDTO activityDetailInfoResDTO = queryFactory.select(Projections.constructor(AdminDTO.ActivityDetailInfoResDTO.class,
                member.name, member.department, member.stdId, partner.partnerName, activity.review,
                activity.activityDate, activity.startTime, activity.endTime, activity.distance))
                .from(activity)
                .leftJoin(member).on(activity.member.eq(member))
                .leftJoin(partner).on(activity.partner.eq(partner))
                .where(activity.activityId.eq(activityId))
                .fetchOne();

        List<MapCapture> mapCaptures = findMapCaptures(activityId);
        activityDetailInfoResDTO.setMapPicture(MapCaptureDTO.MapCaptureResDTO.toDTOList(mapCaptures));

        Activity activityEntity = findActivity(activityId);
        activityDetailInfoResDTO.setTotalTime(activityEntity.getActivityDivision() == 0 ? activityEntity.getOrdinaryTime() : activityEntity.getCareTime());

        return activityDetailInfoResDTO;
    }

    public List<MapCapture> findMapCaptures(Long activityId) {
        return queryFactory.select(mapCapture)
                .from(mapCapture)
                .leftJoin(activity).on(mapCapture.activityId.eq(activity.activityId))
                .where(mapCapture.activityId.eq(activityId))
                .fetch();
    }

    public Activity findActivity(Long activityId) {
        return queryFactory.select(activity)
                .from(activity)
                .where(activity.activityId.eq(activityId))
                .fetchOne();
    }

    public List<AdminDTO.PartnerInfoResDTO> findPartnerInfo(String keyword, String partnerDetail) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(eqName(keyword)).or(eqStdId(keyword)).and(eqPartnerDetail(partnerDetail));

        return queryFactory.select(Projections.constructor(AdminDTO.PartnerInfoResDTO.class,
                member.stdId, member.name, member.department, partner.partnerName,
                partner.partnerBirth, partner.gender, partner.relationship, partner.partnerDivision))
                .from(partner)
                .leftJoin(member).on(member.eq(partner.member))
                .where(booleanBuilder)
                .orderBy(member.name.asc())
                .fetch();
    }

    public BooleanExpression eqPartnerDetail(String partnerDetail) {
        return partner.partnerDetail.eq(partnerDetail);
    }
}
