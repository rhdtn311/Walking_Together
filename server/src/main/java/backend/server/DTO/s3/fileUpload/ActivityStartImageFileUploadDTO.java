package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ActivityStartImageFileUploadDTO extends FileUploadDTO {

    public ActivityStartImageFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "Activity_Start_" + createFileName(file.getOriginalFilename());
    }
}
