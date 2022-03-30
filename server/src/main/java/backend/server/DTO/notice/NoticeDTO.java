package backend.server.DTO.notice;

import backend.server.DTO.s3.fileUpload.NoticeAttachedFileUploadDTO;
import backend.server.DTO.s3.fileUpload.NoticeImageFileUploadDTO;
import backend.server.entity.Notice;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NoticeDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeListResDTO {
        private Long noticeId;
        private String title;
        private String content;
        private LocalDateTime date;

        @Builder
        public NoticeListResDTO(Long noticeId, String title, String content, LocalDateTime date) {
            this.noticeId = noticeId;
            this.title = title;
            this.content = content;
            this.date = date;
        }

        public static NoticeListResDTO entityToDto(Notice notice) {
            return NoticeListResDTO.builder()
                    .noticeId(notice.getNoticeId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .date(notice.getModDate())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeDetailResDTO {
        private Long noticeId;
        private String title;
        private String content;

        private List<String> attachedFiles;
        private List<String> imageFiles;
        private String createTime;

        @Builder
        public NoticeDetailResDTO(Long noticeId, String title, String content, LocalDateTime createTime) {
            this.noticeId = noticeId;
            this.title = title;
            this.content = content;
            this.createTime = createTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        public void setAttachedFiles(List<String> attachedFiles) {
            this.attachedFiles = attachedFiles;
        }

        public void setImageFiles(List<String> imageFiles) {
            this.imageFiles = imageFiles;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeCreationReqDTO {
        private String title;
        private String content;
        private List<MultipartFile> attachedFiles;
        private List<MultipartFile> imageFiles;

        public Notice toNotice() {
            return Notice.builder()
                    .title(this.title)
                    .content(this.content)
                    .build();
        }

        public boolean isAttachedFilesPresent() {
            return this.attachedFiles != null;
        }

        public boolean isImageFilesPresent() {
            return this.imageFiles != null;
        }

        public NoticeImageFileUploadDTO ImageFileToFileUploadDTO() {
            return new NoticeImageFileUploadDTO(this.imageFiles.get(0));
        }

        public NoticeAttachedFileUploadDTO AttachedFileToFileUploadDTO() {
            return new NoticeAttachedFileUploadDTO(this.attachedFiles.get(0));
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeModifyResDTO {
        private String title;
        private String content;

        private List<String> attachedFiles;
        private List<String> imageFiles;

        @Builder
        public NoticeModifyResDTO(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public void setImageFiles(List<String> imageFiles) {
            this.imageFiles = imageFiles;
        }

        public void setAttachedFiles(List<String> attachedFiles) {
            this.attachedFiles = attachedFiles;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeModifyReqDTO {
        private String title;
        private String content;
        private Long noticeId;
        List<MultipartFile> attachedFiles;
        List<MultipartFile> imageFiles;

        public boolean isAttachedFilesPresent() {
            return this.attachedFiles != null;
        }

        public boolean isImageFilesPresent() {
            return this.imageFiles != null;
        }
    }
}
