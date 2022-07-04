package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhotos;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerPhotosRepository extends JpaRepository<PartnerPhotos, Long>, FileDeleteRepository {

    PartnerPhotos findPartnerPhotosByPartner(Partner partner);

    PartnerPhotos findPartnerPhotosByPartner_PartnerId(Long partnerId);

    boolean existsPartnerPhotosByPartner(Partner partner);

    @Override
    default PartnerPhotos find(FileDeleteDTO fileDeleteDTO) {
        return findPartnerPhotosByPartner_PartnerId(fileDeleteDTO.getId());
    }

}
