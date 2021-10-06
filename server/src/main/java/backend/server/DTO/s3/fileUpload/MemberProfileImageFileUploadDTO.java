package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MemberProfileImageFileUploadDTO extends FileUploadDTO {

    public MemberProfileImageFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "User_Profile_Image_" + createFileName(file.getOriginalFilename());
    }
}
