package backend.server.repository;

import backend.server.entity.MemberProfilePictures;
import backend.server.entity.PartnerPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerPhotosRepository extends JpaRepository<PartnerPhotos, Long> {

    public PartnerPhotos findPartnerPhotosByPartnerId(Long partnerId);

    boolean existsPartnerPhotosByPartnerId(Long partnerId);

}
