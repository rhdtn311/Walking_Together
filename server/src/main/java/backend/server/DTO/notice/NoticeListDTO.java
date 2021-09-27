package backend.server.DTO.notice;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NoticeListDTO {

    private Long noticeId;
    private String title;
    private String content;
    private LocalDateTime date;
}
