package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.PartnerProfileImageFileUploadDTO;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhoto;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.PartnerPhotoRepository;
import backend.server.repository.PartnerRepository;
import backend.server.s3.FileUpdateService;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PartnerInfoChangeService {
    private final PartnerRepository partnerRepository;
    private final PartnerPhotoRepository partnerPhotoRepository;

    private final FileUploadService fileUploadService;
    private final FileUpdateService fileUpdateService;

    @Transactional
    public Long updatePartnerInfo(MyPageDTO.PartnerInfoChangeReqDTO partnerInfoChangeReqDTO) {

        Long partnerId = Long.parseLong(partnerInfoChangeReqDTO.getPartnerId());
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(PartnerNotFoundException::new);

        if (partnerInfoChangeReqDTO.isPartnerDetailPresent()) {
            partner.changePartnerDetail(partnerInfoChangeReqDTO.getPartnerDetail());
        }

        if(partnerInfoChangeReqDTO.isPartnerNamePresent()) {
            partner.changePartnerName(partnerInfoChangeReqDTO.getPartnerName());
        }

        if(partnerInfoChangeReqDTO.isPartnerBirthPresent()) {
            partner.changePartnerBirth(partnerInfoChangeReqDTO.replacePartnerBirth());
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
            PartnerPhoto partnerPhoto = PartnerPhoto.builder().build();
            PartnerProfileImageFileUploadDTO partnerProfileImageFileUploadDTO = new PartnerProfileImageFileUploadDTO(partnerInfoChangeReqDTO.getPartnerPhoto());
            if (partner.existsPartnerPhoto()) {
                String fileUrl = fileUpdateService.updateFile(partnerProfileImageFileUploadDTO, partnerPhotoRepository, partner.getPartnerPhoto().getPartnerPhotoId());
                partnerPhoto = savePartnerPhoto(fileUrl, partnerProfileImageFileUploadDTO.getFileName(), partner);
            } else {
                String fileUrl = fileUploadService.uploadFileToS3(partnerProfileImageFileUploadDTO);
                partnerPhoto = savePartnerPhoto(fileUrl, partnerProfileImageFileUploadDTO.getFileName(), partner);
            }
            partner.changePartnerPhoto(partnerPhoto);
        }
        return partnerId;
    }

    private PartnerPhoto savePartnerPhoto(String fileUrl, String fileName, Partner partner) {

        PartnerPhoto partnerPhoto = PartnerPhoto.builder().build();
        if (partner.existsPartnerPhoto()) {
            partnerPhoto = partner.getPartnerPhoto();
            partnerPhoto.changeFileName(fileName);
            partnerPhoto.changeFileUrl(fileUrl);
        } else {
            partnerPhoto = PartnerPhoto.builder()
                    .partnerPhotoName(fileName)
                    .partnerPhotoUrl(fileUrl)
                    .build();

            partnerPhotoRepository.save(partnerPhoto);
        }
        return partnerPhoto;
    }
}
