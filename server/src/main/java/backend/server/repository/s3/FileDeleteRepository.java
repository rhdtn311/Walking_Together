package backend.server.repository.s3;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDeleteRepository {
    public Object find(FileDeleteDTO fileDeleteDTO);
}
