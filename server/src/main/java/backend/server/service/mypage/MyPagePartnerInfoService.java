package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Partner;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.PartnerRepository;
import backend.server.repository.querydsl.MyPageQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPagePartnerInfoService {

    private final MyPageQueryRepository myPageQueryRepository;

    private final PartnerRepository partnerRepository;

    @Transactional(readOnly = true)
    public List<MyPageDTO.MyPagePartnerListResDTO> findPartnersInfo(String stdId) {
        return myPageQueryRepository.findPartnersInfo(stdId);
    }

    // 마이페이지 - 파트너 세부 정보
    @Transactional(readOnly = true)
    public MyPageDTO.MyPagePartnerDetailResDTO findPartnerDetail(Long partnerId) {
        Optional<Partner> partnerOptional = partnerRepository.findPartnerByPartnerId(partnerId);
        if (partnerOptional.isEmpty()) {
            throw new PartnerNotFoundException();
        }

        Partner partner = partnerOptional.get();

        return MyPageDTO.MyPagePartnerDetailResDTO.entityToDTO(partner);
    }
}
