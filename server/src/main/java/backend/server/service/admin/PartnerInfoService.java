package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.repository.querydsl.AdminQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PartnerInfoService {

    private final AdminQueryRepository adminQueryRepository;

    // 파트너 정보 조회
    public List<AdminDTO.PartnerInfoResDTO> getPartnerInfo(String keyword, String partnerDetail) {
        return adminQueryRepository.findPartnerInfo(keyword, partnerDetail);
    }
}
