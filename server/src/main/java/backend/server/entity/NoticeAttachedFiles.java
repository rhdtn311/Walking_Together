package backend.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notice_attached_files")
public class  NoticeAttachedFiles {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeAttachedFilesId;

    private String noticeAttachedFilesUrl;

    private String noticeAttachedFileName;

    private Long noticeId;

    public void changeFileUrl(String fileUrl) {
        this.noticeAttachedFilesUrl = fileUrl;
    }

    public void changeFileName(String fileName) {
        this.noticeAttachedFileName = fileName;
    }
}
