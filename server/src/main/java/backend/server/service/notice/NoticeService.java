package backend.server.service.notice;

import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import backend.server.repository.querydsl.NoticeQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeImagesRepository imagesRepository;
    private final NoticeAttachedFilesRepository attachedFilesRepository;
    private final FileUploadService fileUploadService;
    private final NoticeQueryRepository noticeQueryRepository;


}
