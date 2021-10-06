package backend.server.service.mypage;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.Partner;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.exception.mypageService.PartnerHaveActivityException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerPhotosRepository;
import backend.server.repository.PartnerRepository;
import backend.server.s3.FileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PartnerDeleteService {

    private final PartnerRepository partnerRepository;
    private final ActivityRepository activityRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;
    private final FileDeleteService fileDeleteService;

    public Long deletePartner(Long partnerId) {
        // 파트너가 활동을 가지고 있으면 삭제 불가능
        if (activityRepository.existsActivitiesByPartner_PartnerId(partnerId)) {
            throw new PartnerHaveActivityException();
        }

        // 존재하지 않는 파트너면 삭제 불가능
        if (!partnerRepository.existsPartnerByPartnerId(partnerId)) {
            throw new PartnerNotFoundException();
        }

        fileDeleteService.deleteFile(partnerPhotosRepository, new FileDeleteDTO(partnerId));
        partnerPhotosRepository.delete(partnerPhotosRepository.findPartnerPhotosByPartnerId(partnerId));

        Partner partner = partnerRepository.findById(partnerId).get();
        partnerRepository.delete(partner);

        return partnerId;
    }
}
