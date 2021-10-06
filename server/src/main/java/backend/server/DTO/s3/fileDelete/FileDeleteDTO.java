package backend.server.DTO.s3.fileDelete;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDeleteDTO {
    private String stdId;
    private Long id;

    public FileDeleteDTO(String stdId) {
        this.stdId = stdId;
    }

    public FileDeleteDTO(Long id) {
        this.id = id;
    }

    public FileDeleteDTO(Object id) {
        if (id instanceof String) this.stdId = (String)id;
        else if (id instanceof Long) this.id = (Long)id;
    }
}
