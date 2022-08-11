package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.PartnerProfileImageFileUploadDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhoto;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.PartnerPhotoRepository;
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
    private final PartnerPhotoRepository partnerPhotoRepository;

    private final FileUploadService fileUploadService;

    @Transactional
    public Long createPartner (MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO) {

        Member member = memberRepository.findMemberByStdId(partnerCreationReqDTO.getStdId()).orElseThrow(MemberNotFoundException::new);
        Partner partner = partnerCreationReqDTO.toPartner(member);
        Partner savedPartner = partnerRepository.save(partner);

        if (partnerCreationReqDTO.isPartnerPhotoPresent()) {
            PartnerProfileImageFileUploadDTO partnerProfileImageFileUploadDTO = new PartnerProfileImageFileUploadDTO(partnerCreationReqDTO.getPartnerPhoto());
            String fileUrl = fileUploadService.uploadFileToS3(partnerProfileImageFileUploadDTO);
            PartnerPhoto partnerPhoto = savePartnerPhoto(partnerProfileImageFileUploadDTO.getFileName(), fileUrl);
            partner.changePartnerPhoto(partnerPhoto);
        }

        return savedPartner.getPartnerId();
    }

    private PartnerPhoto savePartnerPhoto(String fileName, String fileUrl) {
        PartnerPhoto partnerPhoto = PartnerPhoto.builder()
                .partnerPhotoName(fileName)
                .partnerPhotoUrl(fileUrl)
                .build();
        return partnerPhotoRepository.save(partnerPhoto);
    }
}
