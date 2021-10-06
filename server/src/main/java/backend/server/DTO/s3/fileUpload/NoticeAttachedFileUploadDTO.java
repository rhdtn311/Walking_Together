package backend.server.DTO.s3.fileUpload;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class NoticeAttachedFileUploadDTO extends FileUploadDTO {

    public NoticeAttachedFileUploadDTO(MultipartFile file) {
        super(file);
        this.fileName = "Notice_Attached_File_" + createFileName(file.getOriginalFilename());
    }
}
