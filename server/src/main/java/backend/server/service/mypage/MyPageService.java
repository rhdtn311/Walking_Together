package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.myPage.MyPageMemberDTO;
import backend.server.DTO.myPage.MyPagePartnerDTO;
import backend.server.entity.*;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.*;
import backend.server.repository.querydsl.MyPageQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final ActivityRepository activityRepository;
    private final MemberProfilePicturesRepository memberProfilePicturesRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;

    private final MyPageQueryRepository myPageQueryRepository;

    private final FileUploadService fileUploadService;

    private final PasswordEncoder passwordEncoder;

    public Long deletePartner(Long partnerId) {

        // 파트너가 활동을 가지고 있으면 삭제 불가능
        Optional<List<Activity>> activity = activityRepository.findActivitiesByPartner_PartnerId(partnerId);
        if (activity.get().size() != 0) {
            return 400L;
        }

        // 존재하지 않는 파트너면 삭제 불가능
        Optional<Partner> partnerOptional = partnerRepository.findById(partnerId);
        if (partnerOptional.isEmpty()) {
            return 404L;
        }

        Partner partner = partnerOptional.get();

        partnerRepository.delete(partner);

        return partnerId;
    }
}
