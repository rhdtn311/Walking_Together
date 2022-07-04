package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.PartnerProfileImageFileUploadDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhotos;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.PartnerPhotosRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PartnerCreationService {

    private final MemberRepository memberRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;

    private final FileUploadService fileUploadService;

    @Transactional
    public Long createPartner (MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO) {

        Member member = memberRepository.findMemberByStdId(partnerCreationReqDTO.getStdId()).orElseThrow(MemberNotFoundException::new);
        Partner partner = partnerCreationReqDTO.toPartner(member);
        Partner savedPartner = partnerRepository.save(partner);

        if (partnerCreationReqDTO.isPartnerPhotoPresent()) {
            PartnerProfileImageFileUploadDTO partnerProfileImageFileUploadDTO = new PartnerProfileImageFileUploadDTO(partnerCreationReqDTO.getPartnerPhoto());
            String fileUrl = fileUploadService.uploadFileToS3(partnerProfileImageFileUploadDTO);
            savePartnerPhoto(savedPartner, partnerProfileImageFileUploadDTO.getFileName(), fileUrl);
        }

        return savedPartner.getPartnerId();
    }

    private void savePartnerPhoto(Partner partner, String fileName, String fileUrl) {
        PartnerPhotos partnerPhoto = PartnerPhotos.builder()
                .partner(partner)
                .partnerPhotoName(fileName)
                .partnerPhotoUrl(fileUrl)
                .build();
        partnerPhotosRepository.save(partnerPhoto);
    }
}
