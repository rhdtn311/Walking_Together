package backend.server.DTO.s3.fileUpload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class FileUploadDTO {
    public String fileName;
    MultipartFile file;

    @Builder
    public FileUploadDTO(MultipartFile file) {
        this.file = file;
    }

    protected String createFileName(String originalFileName) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return date.format(new Date()) + "-" + originalFileName;
    }
}
