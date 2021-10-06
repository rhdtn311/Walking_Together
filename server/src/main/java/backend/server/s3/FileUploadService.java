package backend.server.s3;

import backend.server.DTO.s3.fileUpload.FileUploadDTO;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final S3Service s3Service;

    public String uploadFileToS3(FileUploadDTO fileUploadDTO) {
        MultipartFile file = fileUploadDTO.getFile();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileUploadDTO.getFileName());
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",file.getOriginalFilename()));
        }
        return s3Service.getFileUrl(fileUploadDTO.getFileName());
    }
}
