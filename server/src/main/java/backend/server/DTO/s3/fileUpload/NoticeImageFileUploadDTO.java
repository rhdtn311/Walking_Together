package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class NoticeImageFileUploadDTO extends FileUploadDTO {

    public NoticeImageFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "Notice_Image_" + createFileName(file.getOriginalFilename());
    }
}
