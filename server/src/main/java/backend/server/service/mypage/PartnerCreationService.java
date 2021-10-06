package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.PartnerProfileImageFileUploadDTO;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhotos;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.PartnerPhotosRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PartnerCreationService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;

    private final FileUploadService fileUploadService;

    @Transactional
    public Long createPartner (MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO) {
        if (!userRepository.existsMemberByStdId(partnerCreationReqDTO.getStdId())) {
            throw new MemberNotFoundException();
        }
        Partner partner = partnerCreationReqDTO.partnerCreationReqDTOToPartner();
        Partner savedPartner = partnerRepository.save(partner);

        if (partnerCreationReqDTO.isPartnerPhotoPresent()) {
            PartnerProfileImageFileUploadDTO partnerProfileImageFileUploadDTO = new PartnerProfileImageFileUploadDTO(partnerCreationReqDTO.getPartnerPhoto());
            String fileUrl = fileUploadService.uploadFileToS3(partnerProfileImageFileUploadDTO);
            savePartnerPhoto(savedPartner.getPartnerId(), partnerProfileImageFileUploadDTO.getFileName(), fileUrl);
        }

        return savedPartner.getPartnerId();
    }

    private void savePartnerPhoto(Long partnerId, String fileName, String fileUrl) {
        PartnerPhotos partnerPhoto = PartnerPhotos.builder()
                .partnerId(partnerId)
                .partnerPhotoName(fileName)
                .partnerPhotoUrl(fileUrl)
                .build();
        partnerPhotosRepository.save(partnerPhoto);
    }
}
