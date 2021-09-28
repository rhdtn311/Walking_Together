package backend.server.DTO.notice;

import backend.server.entity.Notice;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {

    private String title;
    private String content;
    private LocalDateTime createTime;
    private Long noticeId;


    public Notice dtoToEntity() {
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .build();
        return notice;
    }

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
}
