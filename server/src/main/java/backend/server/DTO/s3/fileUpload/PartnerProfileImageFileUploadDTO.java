package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PartnerProfileImageFileUploadDTO extends FileUploadDTO {

    public PartnerProfileImageFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "Partner_Profile_Image_" + createFileName(file.getOriginalFilename());
    }
}
