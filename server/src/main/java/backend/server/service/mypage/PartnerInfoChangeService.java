package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Partner;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.PartnerPhotosRepository;
import backend.server.repository.PartnerRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PartnerInfoChangeService {
    private final PartnerRepository partnerRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;

    private final FileUploadService fileUploadService;

    @Transactional
    public Long changePartnerInfo(MyPageDTO.PartnerInfoChangeReqDTO partnerInfoChangeReqDTO) {

        Long partnerId = Long.parseLong(partnerInfoChangeReqDTO.getPartnerId());
        if (!partnerRepository.existsPartnerByPartnerId(partnerId)) {
            throw new PartnerNotFoundException();
        }

        Partner partner = partnerRepository.findById(partnerId).get();
        if (partnerInfoChangeReqDTO.isPartnerDetailPresent()) {
            partner.changePartnerDetail(partnerInfoChangeReqDTO.getPartnerDetail());
        }

        if(partnerInfoChangeReqDTO.isPartnerNamePresent()) {
            partner.changePartnerName(partnerInfoChangeReqDTO.getPartnerName());
        }

        if(partnerInfoChangeReqDTO.isPartnerBirthPresent()) {
            partner.changePartnerBirth(partnerInfoChangeReqDTO.partnerBirthReplace());
        }

        if(partnerInfoChangeReqDTO.isSelectionReasonPresent()) {
            partner.changePartnerSelectionReason(partnerInfoChangeReqDTO.getSelectionReason());
        }

        if(partnerInfoChangeReqDTO.isRelationshipPresent()) {
            partner.changePartnerRelationship(partnerInfoChangeReqDTO.getRelationship());
        }

        if(partnerInfoChangeReqDTO.isGenderPresent()) {
            partner.changePartnerGender(partnerInfoChangeReqDTO.getGender());
        }

        if (partnerInfoChangeReqDTO.isPartnerPhotoPresent()) {
            if (partnerPhotosRepository.existsPartnerPhotosByPartnerId(partnerId)) {
                fileUploadService.updatePartnerPhoto(partnerInfoChangeReqDTO.getPartnerPhoto(), partnerId);
            } else {
                fileUploadService.uploadPartnerPhoto(partnerInfoChangeReqDTO.getPartnerPhoto(), partnerId);
            }
        }

        return partnerId;
    }
}
