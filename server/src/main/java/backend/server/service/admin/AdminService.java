package backend.server.service.admin;

import backend.server.DTO.admin.*;
import backend.server.entity.Activity;
import backend.server.entity.MapCapture;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import backend.server.repository.querydsl.AdminQueryRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
// 관리자 페이지
public class AdminService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PartnerRepository partnerRepository;
    private final MapCaptureRepository mapCaptureRepository;
    private final AdminQueryRepository adminQueryRepository;

    // 파트너 정보 조회
    public List<PartnerInfoDTO> partnerInfo(String keyword, String partnerDetail) {

        List<Tuple> tuples = partnerRepository.partnerInfo(keyword, partnerDetail);
        List<PartnerInfoDTO> partnerList = new ArrayList<>();

        tuples.forEach(partner -> {
            //member.name, member.stdId, member.department, partner.partnerName,
            //                partner.gender, partner.partnerBirth, partner.relationship, partner.partnerDivision

            PartnerInfoDTO dto = PartnerInfoDTO.builder()
                    .stdName(partner.get(0,String.class))
                    .stdId(partner.get(1,String.class))
                    .department(partner.get(2,String.class))
                    .partnerName(partner.get(3,String.class))
                    .partnerGender(partner.get(4,String.class))
                    .partnerBirth(partner.get(5,String.class))
                    .relationship(partner.get(6,String.class))
                    .partnerDivision(partner.get(7,Integer.class))
                    .build();

            partnerList.add(dto);
        });

        return partnerList;
    }
}
