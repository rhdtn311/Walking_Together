package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.PartnerProfileImageFileUploadDTO;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhotos;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.PartnerPhotosRepository;
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
    private final PartnerPhotosRepository partnerPhotosRepository;

    private final FileUploadService fileUploadService;
    private final FileUpdateService fileUpdateService;

    @Transactional
    public Long updatePartnerInfo(MyPageDTO.PartnerInfoChangeReqDTO partnerInfoChangeReqDTO) {

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
            PartnerProfileImageFileUploadDTO partnerProfileImageFileUploadDTO = new PartnerProfileImageFileUploadDTO(partnerInfoChangeReqDTO.getPartnerPhoto());
            if (partnerPhotosRepository.existsPartnerPhotosByPartnerId(partnerId)) {
                String fileUrl = fileUpdateService.updateFile(partnerProfileImageFileUploadDTO, partnerPhotosRepository, partnerId);
                savePartnerPhoto(fileUrl, partnerProfileImageFileUploadDTO.getFileName(), partnerId);
            } else {
                String fileUrl = fileUploadService.uploadFileToS3(partnerProfileImageFileUploadDTO);
                savePartnerPhoto(fileUrl, partnerProfileImageFileUploadDTO.fileName, partnerId);
            }
        }
        return partnerId;
    }

    private void savePartnerPhoto(String fileUrl, String fileName, Long partnerId) {
        if (partnerPhotosRepository.existsPartnerPhotosByPartnerId(partnerId)) {
            PartnerPhotos partnerPhoto = partnerPhotosRepository.findPartnerPhotosByPartnerId(partnerId);
            partnerPhoto.changeFileName(fileName);
            partnerPhoto.changeFileUrl(fileUrl);
        } else {
            PartnerPhotos partnerPhoto = PartnerPhotos.builder()
                    .partnerId(partnerId)
                    .partnerPhotoName(fileName)
                    .partnerPhotoUrl(fileUrl)
                    .build();

            partnerPhotosRepository.save(partnerPhoto);
        }
    }
}
