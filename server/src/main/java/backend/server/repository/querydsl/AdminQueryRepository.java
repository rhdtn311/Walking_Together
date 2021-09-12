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

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AdminQueryRepository {

    private final JPQLQueryFactory queryFactory;

    public List<AdminDTO.MemberResDTO> findMemberInfo(String keyword) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(eqName(keyword)).or(eqStdId(keyword));

        return queryFactory.select(Projections.constructor(AdminDTO.MemberResDTO.class,
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
}
