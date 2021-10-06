package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ActivityEndImageFileUploadDTO extends FileUploadDTO {

    public ActivityEndImageFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "Activity_End_" + createFileName(file.getOriginalFilename());
    }
}
