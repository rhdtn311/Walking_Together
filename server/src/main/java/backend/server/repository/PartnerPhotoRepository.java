package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhoto;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerPhotoRepository extends JpaRepository<PartnerPhoto, Long>, FileDeleteRepository {

//    PartnerPhoto findPartnerPhotosByPartner(Partner partner);
//
//    PartnerPhoto findPartnerPhotosByPartner_PartnerId(Long partnerId);
//
//    boolean existsPartnerPhotosByPartner(Partner partner);

    @Override
    default PartnerPhoto find(FileDeleteDTO fileDeleteDTO) {
        return findById(fileDeleteDTO.getId()).get();
    }

}
