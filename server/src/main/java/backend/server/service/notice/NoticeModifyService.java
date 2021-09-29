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
public class NoticeModifyService {

    private final NoticeRepository noticeRepository;

    private final NoticeQueryRepository noticeQueryRepository;

    public NoticeDTO.NoticeModifyResDTO getNoticeInfo(Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new NoticeNotFoundException();
        }

        NoticeDTO.NoticeModifyResDTO noticeModifyResDTO = noticeQueryRepository.findNoticeModify(noticeId);
        List<String> noticeAttachedFileURLs = noticeQueryRepository.findNoticeAttachedFileURLs(noticeId);
        List<String> noticeImageFileURLs = noticeQueryRepository.findNoticeImageFileURLs(noticeId);

        noticeModifyResDTO.setAttachedFiles(noticeAttachedFileURLs);
        noticeModifyResDTO.setImageFiles(noticeImageFileURLs);

        return noticeModifyResDTO;
    }
}
