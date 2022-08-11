package backend.server.service.mypage;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Partner;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.exception.mypageService.PartnerHaveActivityException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerPhotoRepository;
import backend.server.repository.PartnerRepository;
import backend.server.s3.FileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Service
public class PartnerDeleteService {

    private final PartnerRepository partnerRepository;
    private final ActivityRepository activityRepository;
    private final PartnerPhotoRepository partnerPhotoRepository;
    private final FileDeleteService fileDeleteService;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long deletePartner(Long partnerId) {

        // 존재하지 않는 파트너면 삭제 불가능
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(PartnerNotFoundException::new);

        // 파트너가 활동을 가지고 있으면 삭제 불가능
        if (activityRepository.existsActivitiesByPartner(partner)) {
            throw new PartnerHaveActivityException();
        }

        if (partner.existsPartnerPhoto()) {
            fileDeleteService.deleteFile(partnerPhotoRepository, new FileDeleteDTO(partner.getPartnerPhoto().getPartnerPhotoId()));
            partnerPhotoRepository.delete(partner.getPartnerPhoto());
        }

        partnerRepository.delete(partner);

        return partnerId;
    }
}
