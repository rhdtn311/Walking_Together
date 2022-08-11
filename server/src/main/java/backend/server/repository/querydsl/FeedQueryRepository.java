package backend.server.repository.querydsl;

import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.common.MapCaptureDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.Certification;
import backend.server.entity.MapCapture;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static backend.server.entity.QMember.member;
import static backend.server.entity.QActivity.activity;
import static backend.server.entity.QPartner.partner;
import static backend.server.entity.QMapCapture.mapCapture;
import static backend.server.entity.QCertification.certification;

@RequiredArgsConstructor
@Repository
public class FeedQueryRepository {

    private final JPQLQueryFactory queryFactory;

    public List<FeedDTO.FeedMainResDTO> findFeedMain(String stdId, String sort) {

        return  queryFactory.select(Projections.constructor(FeedDTO.FeedMainResDTO.class,
                activity.activityId, activity.activityStatus, activity.distance, activity.activityDate,
                activity.activityDivision, partner.partnerName))
                .from(activity)
                .leftJoin(member).on(member.eq(activity.member))
                .leftJoin(partner).on(partner.eq(activity.partner))
                .where(eqStdId(stdId))
                .orderBy(sort.equals("desc") ? activity.activityDate.desc() : activity.activityDate.asc())
                .fetch();
    }

    public BooleanExpression eqStdId(String stdId) {
        if (StringUtils.isEmpty(stdId)) {
            return null;
        }
        return member.stdId.eq(stdId);
    }

    public FeedDTO.FeedDetailResDTO findFeedDetail (Long activityId) {
        FeedDTO.FeedDetailResDTO feedDetailResDTO = queryFactory.select(Projections.constructor(FeedDTO.FeedDetailResDTO.class,
                activity.activityDate, partner.partnerName, activity.startTime,
                activity.endTime, activity.activityDivision, activity.review))
                .from(activity)
                .leftJoin(member).on(member.eq(activity.member))
                .leftJoin(partner).on(partner.eq(activity.partner))
                .where(eqActivityId(activityId))
                .fetchOne();

        return feedDetailResDTO;
    }

    public BooleanExpression eqActivityId(Long activityId) {
        if (activityId == null) {
            return null;
        }
        return activity.activityId.eq(activityId);
    }

    public List<CertificationDTO> findCertification(LocalDate from, LocalDate to, String stdId) {
        List<Certification> certificationList = queryFactory.select(certification)
                .from(certification)
                .where(eqCertificationStdId(stdId).and(betweenDate(from, to)))
                .fetch();

        return CertificationDTO.toDTOList(certificationList);
    }

    public BooleanExpression eqCertificationStdId(String stdId) {
        if (StringUtils.isEmpty(stdId)) {
            return null;
        }
        return certification.stdId.eq(stdId);
    }

    public BooleanExpression betweenDate(LocalDate from, LocalDate to) {
        return certification.activityDate.between(from, to);
    }
}







