package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.MemberProfilePictures;
import backend.server.entity.PartnerPhotos;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerPhotosRepository extends JpaRepository<PartnerPhotos, Long>, FileDeleteRepository {

    public PartnerPhotos findPartnerPhotosByPartnerId(Long partnerId);

    boolean existsPartnerPhotosByPartnerId(Long partnerId);

    @Override
    default PartnerPhotos find(FileDeleteDTO fileDeleteDTO) {
        return findPartnerPhotosByPartnerId(fileDeleteDTO.getId());
    }

}
