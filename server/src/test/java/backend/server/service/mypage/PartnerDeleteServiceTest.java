package backend.server.service.mypage;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhoto;
import backend.server.exception.mypageService.PartnerHaveActivityException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerPhotoRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PartnerDeleteServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    PartnerPhotoRepository partnerPhotoRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    PartnerDeleteService partnerDeleteService;

    @MockBean
    private FileUploadService fileUploadService;

    Member member;
    Partner partner;
    Partner activePartner;
    PartnerPhoto partnerPhoto;
    Activity activity;

    @BeforeEach
    void init() {
        member = Member.builder()
                .name("김토토")
                .stdId("2015100885")
                .email("rhdtn311@naver.com")
                .password("password")
                .department("컴퓨터")
                .birth("19960311")
                .phoneNumber("01039281329")
                .build();
        memberRepository.save(member);

        partnerPhoto = PartnerPhoto.builder()
                .partnerPhotoName("partnerPhotoName")
                .partnerPhotoUrl("partnerPhotoUrl")
                .build();
        partnerPhotoRepository.save(partnerPhoto);

        partner = Partner.builder()
                .member(member)
                .partnerName("partner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .partnerPhoto(partnerPhoto)
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .partnerPhoto(partnerPhoto)
                .build();
        partnerRepository.save(partner);

        activePartner= Partner.builder()
                .member(member)
                .partnerName("activePartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();
        partnerRepository.save(activePartner);

        activity = Activity.builder()
                .member(member)
                .partner(activePartner)
                .distance(0L)
                .activityDate(LocalDate.of(2021, 1, 1))
                .activityStatus(1)
                .activityDivision(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        activityRepository.save(activity);
    }

    @Test
    @DisplayName("파트너 정상 삭제 확인")
    void deletePartner() {

        // when
        partnerDeleteService.deletePartner(partner.getPartnerId());
        Optional<Partner> findPartner = partnerRepository.findById(partner.getPartnerId());
        Optional<PartnerPhoto> findPartnerPhoto = partnerPhotoRepository.findById(partnerPhoto.getPartnerPhotoId());

        // then
        assertThat(findPartner).isNotPresent();
        assertThat(findPartnerPhoto).isNotPresent();
    }

    @Test
    @DisplayName("파트너가 활동 중인 경우 삭제 불가능")
    void activePartnerNotDelete() {

        // then
        assertThrows(PartnerHaveActivityException.class, () -> {
            partnerDeleteService.deletePartner(activePartner.getPartnerId());
        });
    }

}