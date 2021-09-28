package backend.server.repository.querydsl;

import backend.server.DTO.notice.NoticeDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static backend.server.entity.QNotice.notice;
import static backend.server.entity.QNoticeAttachedFiles.noticeAttachedFiles;
import static backend.server.entity.QNoticeImages.noticeImages;

@RequiredArgsConstructor
@Repository
public class NoticeQueryRepository {

    private final JPQLQueryFactory jpqlQueryFactory;

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

    public NoticeDTO.NoticeDetailResDTO findNoticeDetail(Long noticeId) {
        return jpqlQueryFactory.select(Projections.constructor(NoticeDTO.NoticeDetailResDTO.class,
                notice.noticeId, notice.title, notice.content, notice.createDate))
                .from(notice)
                .where(eqNoticeIdAndNotice(noticeId))
                .fetchOne();
    }

    private BooleanExpression eqNoticeIdAndNotice(Long noticeId) {
        return notice.noticeId.eq(noticeId);
    }

    public List<String> findNoticeImageFileURLs(Long noticeId) {
        return jpqlQueryFactory.select(noticeImages.noticeImageUrl)
                .from(noticeImages)
                .where(eqNoticeIdAndImage(noticeId))
                .fetch();
    }

    private BooleanExpression eqNoticeIdAndImage(Long noticeId) {
        return noticeImages.noticeId.eq(noticeId);
    }

    public List<String> findNoticeAttachedFileURLs(Long noticeId) {
        return jpqlQueryFactory.select(noticeAttachedFiles.noticeAttachedFilesUrl)
                .from(noticeAttachedFiles)
                .where(eqNoticeIdAndAttachedFiles(noticeId))
                .fetch();
    }

    private BooleanExpression eqNoticeIdAndAttachedFiles(Long noticeId) {
        return noticeAttachedFiles.noticeId.eq(noticeId);
    }
}
