package backend.server.s3;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.DTO.s3.fileUpload.FileUploadDTO;
import backend.server.repository.s3.FileDeleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileUpdateService {
    private final FileUploadService fileUploadService;
    private final FileDeleteService fileDeleteService;

    public String updateFile(FileUploadDTO fileUploadDTO, FileDeleteRepository repository, Object id) {
        FileDeleteDTO fileDeleteDTO = new FileDeleteDTO(id);
        fileDeleteService.deleteFile(repository, fileDeleteDTO);
        String fileUrl = fileUploadService.uploadFileToS3(fileUploadDTO);

        return fileUrl;
    }
}
