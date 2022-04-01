package backend.server.service.notice;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.entity.Notice;
import backend.server.entity.NoticeAttachedFiles;
import backend.server.entity.NoticeImages;
import backend.server.repository.NoticeAttachedFilesRepository;
import backend.server.repository.NoticeImagesRepository;
import backend.server.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class NoticeCreationServiceTest {

    @Autowired
    NoticeCreationService noticeCreationService;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeImagesRepository noticeImagesRepository;

    @Autowired
    NoticeAttachedFilesRepository noticeAttachedFilesRepository;

    @Test
    @DisplayName("공지사항 생성 확인")
    void createNotice() throws IOException {

        // given
        List<MultipartFile> attachFiles = new ArrayList<>();
        List<MultipartFile> imageFiles = new ArrayList<>();

        MockMultipartFile imageFile1 = getMockMultipartFile("imageFile1", "png", "src/test/java/imageFiles/ActivityEndImage.png");
        MockMultipartFile imageFile2 = getMockMultipartFile("imageFile2", "png", "src/test/java/imageFiles/ActivityEndImage.png");
        MockMultipartFile attachFile1 = getMockMultipartFile("attachFile1", "png", "src/test/java/imageFiles/ActivityEndImage.png");
        MockMultipartFile attachFile2 = getMockMultipartFile("attachFile2", "png", "src/test/java/imageFiles/ActivityEndImage.png");

        attachFiles.add(attachFile1);
        attachFiles.add(attachFile2);
        imageFiles.add(imageFile1);
        imageFiles.add(imageFile2);

        NoticeDTO.NoticeCreationReqDTO noticeCreationReqDTO = NoticeDTO.NoticeCreationReqDTO.builder()
                .title("title")
                .content("content")
                .attachedFiles(attachFiles)
                .imageFiles(imageFiles)
                .build();

        // when
        Long noticeId = noticeCreationService.saveNotice(noticeCreationReqDTO);
        Notice findNotice = noticeRepository.findById(noticeId).get();
        List<NoticeImages> findNoticeImages = noticeImagesRepository.findNoticeImagesByNoticeId(noticeId);
        List<NoticeAttachedFiles> findNoticeAttachedFiles = noticeAttachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);

        // then
        assertThat(findNotice.getTitle()).isEqualTo("title");
        assertThat(findNotice.getContent()).isEqualTo("content");
        assertThat(findNoticeImages.size()).isEqualTo(2);
        assertThat(findNoticeAttachedFiles.size()).isEqualTo(2);
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }


}