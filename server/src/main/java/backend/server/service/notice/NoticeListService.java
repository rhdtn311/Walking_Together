package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.entity.Notice;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeListService {

    private final NoticeQueryRepository noticeQueryRepository;

    private final NoticeRepository noticeRepository;

    public PageResultDTO<NoticeDTO.NoticeListResDTO, Notice> findNoticeList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("noticeId").descending());

        BooleanBuilder booleanBuilder = noticeQueryRepository.getNoticeSearch(requestDTO.getKeyword());

        Page<Notice> result = noticeRepository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, Notice::noticeToNoticeListResDTO);
    }
}
