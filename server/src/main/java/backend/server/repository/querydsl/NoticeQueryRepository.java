package backend.server.repository.querydsl;

import backend.server.DTO.page.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static backend.server.entity.QNotice.notice;

@RequiredArgsConstructor
@Repository
public class NoticeQueryRepository {

    public BooleanBuilder getNoticeSearch(String keyword) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression expression = notice.noticeId.gt(0L);

        booleanBuilder.and(expression);

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(keyword != null) {
            conditionBuilder.or(notice.content.contains(keyword));
            conditionBuilder.or(notice.title.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
