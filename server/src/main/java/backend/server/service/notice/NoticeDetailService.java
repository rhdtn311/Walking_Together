package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.exception.noticeService.NoticeNotFoundException;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeDetailService {

    private final NoticeRepository noticeRepository;

    private final NoticeQueryRepository noticeQueryRepository;

    public NoticeDTO.NoticeDetailResDTO getNoticeDetail(Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new NoticeNotFoundException();
        }
        NoticeDTO.NoticeDetailResDTO noticeDetailResDTO = noticeQueryRepository.findNoticeDetail(noticeId);
        List<String> noticeImageFileURLs = noticeQueryRepository.findNoticeImageFileURLs(noticeId);
        List<String> noticeAttachedFileURLs = noticeQueryRepository.findNoticeAttachedFileURLs(noticeId);

        noticeDetailResDTO.setImageFiles(noticeImageFileURLs);
        noticeDetailResDTO.setAttachedFiles(noticeAttachedFileURLs);

        return noticeDetailResDTO;
    }
}
