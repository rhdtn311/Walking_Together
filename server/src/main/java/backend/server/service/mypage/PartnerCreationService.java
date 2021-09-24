package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Partner;
import backend.server.exception.activityService.MemberNotFoundException;
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

    private final FileUploadService fileUploadService;

    @Transactional
    public Long createPartner (MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO) {
        if (!userRepository.existsMemberByStdId(partnerCreationReqDTO.getStdId())) {
            throw new MemberNotFoundException();
        }
        Partner partner = partnerCreationReqDTO.partnerCreationReqDTOToPartner();
        Partner savedPartner = partnerRepository.save(partner);

        if (partnerCreationReqDTO.isPartnerPhoto()) {
            fileUploadService.uploadPartnerPhoto(partnerCreationReqDTO.getPartnerPhoto(), savedPartner.getPartnerId());
        }

        return partner.getPartnerId();
    }
}
