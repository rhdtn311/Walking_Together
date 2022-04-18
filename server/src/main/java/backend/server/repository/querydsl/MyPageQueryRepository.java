package backend.server.repository.querydsl;

import backend.server.DTO.myPage.MyPageDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import static backend.server.entity.QMember.member;
import static backend.server.entity.QPartner.partner;

@RequiredArgsConstructor
@Repository
public class MyPageQueryRepository {

    private final JPQLQueryFactory queryFactory;

    public List<MyPageDTO.MyPagePartnerListResDTO> findPartnersInfo(String stdId) {
        return queryFactory.select(Projections.constructor(MyPageDTO.MyPagePartnerListResDTO.class,
                partner.partnerName, partner.partnerId, partner.partnerDetail, partner.partnerBirth))
                .from(partner)
                .leftJoin(member).on(member.eq(partner.member))
                .where(eqStdId(stdId))
                .fetch();
    }

    public BooleanExpression eqStdId(String stdId) {
        if (StringUtils.isEmpty(stdId)) {
            return null;
        }
        return partner.member.stdId.eq(stdId);
    }
}
