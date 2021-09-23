package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.myPage.MyPageMemberDTO;
import backend.server.DTO.myPage.MyPagePartnerDTO;
import backend.server.entity.*;
import backend.server.exception.activityService.MemberNotFoundException;
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

    // 마이페이지 - 파트너 세부 정보
    @Transactional(readOnly = true)
    public MyPagePartnerDTO myPagePartnerDetail(Long partnerId) {

        Optional<Partner> partnerInformation = partnerRepository.getPartnerInformation(partnerId);

        if (partnerInformation.isEmpty()) {
            return null;
        }
        Partner partner = partnerInformation.get();

        return MyPagePartnerDTO.builder()
                .partnerName(partner.getPartnerName())
                .partnerDetail(partner.getPartnerDetail())
                .partnerBirth(partner.getPartnerBirth())
                .gender(partner.getGender())
                .selectionReason(partner.getSelectionReason())
                .relationship(partner.getRelationship())
                .build();
    }

    // 마이페이지 - 파트너 생성
    @Transactional
    public Long createPartner (MyPagePartnerDTO partnerDTO, MultipartFile partnerPhoto) {

        Partner partner = partnerDtoToEntity(partnerDTO);
        // 파트너 정보 저장 (사진 제외)
        Partner savedPartner = partnerRepository.save(partner);

        // 파트너 사진이 필수이면 없어도 됌
        if (partnerPhoto != null) {
            fileUploadService.uploadPartnerPhoto(partnerPhoto, savedPartner.getPartnerId());
        }

        return partner.getPartnerId();
    }

    private Partner partnerDtoToEntity(MyPagePartnerDTO partnerDTO) {

        String stdId = partnerDTO.getStdId();
        Optional<Member> member = userRepository.findMemberByStdId(stdId);

        if(member.isEmpty()) {
            return null;
        }

        int partnerDivision;

        if (partnerDTO.getPartnerDetail().equals("o")) {
            partnerDivision = 0;
        } else {
            partnerDivision = 1;
        }

        return Partner.builder()
                .member(Member.builder().stdId(partnerDTO.getStdId()).build())
                .partnerName(partnerDTO.getPartnerName())
                .partnerBirth(partnerDTO.getPartnerBirth())
                .gender(partnerDTO.getGender())
                .selectionReason(partnerDTO.getSelectionReason())
                .partnerDetail(partnerDTO.getPartnerDetail())
                .relationship(partnerDTO.getRelationship())
                .partnerDivision(partnerDivision)
                .build();
    }

    // 파트너 수정
    @Transactional
    public Long updatePartner(MyPagePartnerDTO partnerDTO, MultipartFile partnerPhoto) {

        Optional<Partner> savedPartner = partnerRepository.findById(partnerDTO.getPartnerId());

        if(savedPartner.isEmpty()) {
            return null;
        }

        Partner partner = savedPartner.get();
        System.out.println(partner);

        if(partnerDTO.getPartnerDetail() != null) {
            partner.changePartnerDetail(partnerDTO.getPartnerDetail());
        }

        if(partnerDTO.getPartnerName() != null) {
            System.out.println("파트너 이름");
            partner.changePartnerName(partnerDTO.getPartnerName());
            System.out.println(partnerDTO.getPartnerName());
            System.out.println(partner.getPartnerName());
        }

        if(partnerDTO.getPartnerBirth() != null) {

            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer birth = stringBuffer.append(partnerDTO.getPartnerBirth());

            birth.replace(4,5,"/");
            birth.replace(7,8,"/");

            partner.changePartnerBirth(birth.toString());
        }

        if(partnerDTO.getSelectionReason() != null) {
            partner.changePartnerSelectionReason(partnerDTO.getSelectionReason());
        }

        if(partnerDTO.getRelationship() != null) {
            partner.changePartnerRelationship(partnerDTO.getRelationship());
        }

        if(partnerDTO.getGender() != null) {
            partner.changePartnerGender(partnerDTO.getGender());
        }

        if (partnerPhoto != null) {
            PartnerPhotos partnerPhotos = partnerPhotosRepository.findPartnerPhotosByPartnerId(partnerDTO.getPartnerId());

            fileUploadService.deletePartnerPhoto(partnerDTO.getPartnerId());
            partnerPhotosRepository.delete(partnerPhotos);

            fileUploadService.uploadPartnerPhoto(partnerPhoto, partnerDTO.getPartnerId());
        }

        return partnerDTO.getPartnerId();
    }

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
