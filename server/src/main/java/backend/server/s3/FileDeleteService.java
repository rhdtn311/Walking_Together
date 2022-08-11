package backend.server.s3;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.*;
import backend.server.repository.s3.FileDeleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileDeleteService {
    private final S3Service s3Service;

    public void deleteFile(FileDeleteRepository repository, FileDeleteDTO fileDeleteDTO) {
        Object file = repository.find(fileDeleteDTO);
        if (file instanceof MemberProfilePictures) {
            s3Service.deleteFile(((MemberProfilePictures) file).getProfilePictureName());
        } else if (file instanceof PartnerPhoto) {
            s3Service.deleteFile(((PartnerPhoto)file).getPartnerPhotoName());
        } else if (file instanceof List) {
            ((List<?>) file).forEach(activityCheckFile -> { if (activityCheckFile instanceof ActivityCheckImages)
                    s3Service.deleteFile(((ActivityCheckImages)activityCheckFile).getImageName());});
        } else if (file instanceof NoticeAttachedFiles) {
            s3Service.deleteFile(((NoticeAttachedFiles) file).getNoticeAttachedFileName());
        } else if (file instanceof NoticeImages) {
            s3Service.deleteFile(((NoticeImages) file).getNoticeImageName());
        }
    }
}
