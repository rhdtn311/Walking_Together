package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.entity.PartnerPhotos;
import backend.server.repository.PartnerPhotosRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.MemberRepository;
import backend.server.s3.FileUploadService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
@Transactional
class PartnerInfoChangeServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    PartnerPhotosRepository partnerPhotosRepository;

    @Autowired
    PartnerInfoChangeService partnerInfoChangeService;

    @MockBean
    private FileUploadService fileUploadService;

    Member member;
    Partner partner;
    PartnerPhotos partnerPhoto;

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

        partner = Partner.builder()
                .member(member)
                .partnerName("partner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();
        partnerRepository.save(partner);

        partnerPhoto = PartnerPhotos.builder()
                .partnerId(partner.getPartnerId())
                .partnerPhotoName("partnerPhotoName")
                .partnerPhotoUrl("partnerPhotoUrl")
                .build();
        partnerPhotosRepository.save(partnerPhoto);

    }

    @Test
    @DisplayName("파트너 이름 변경")
    void changePartnerName() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .partnerName("changePartnerName")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getPartnerName()).isEqualTo("changePartnerName");
    }

    @Test
    @DisplayName("파트너 디테일 변경")
    void changePartnerDetail() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .partnerDetail("d")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getPartnerDetail()).isEqualTo("d");
    }

    @Test
    @DisplayName("파트너 생일 변경")
    void changePartnerBirth() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .partnerBirth("1993-03-11")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getPartnerBirth()).isEqualTo("1993/03/11");
    }

    @Test
    @DisplayName("파트너 선택 이유 변경")
    void changePartnerSelection() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .selectionReason("new selection reason")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getSelectionReason()).isEqualTo("new selection reason");
    }

    @Test
    @DisplayName("파트너 관계 변경")
    void changePartnerRelationship() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .relationship("change relationship")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getRelationship()).isEqualTo("change relationship");
    }

    @Test
    @DisplayName("파트너 성별 변경")
    void changePartnerGender() {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .gender("new gender")
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        Partner findPartner = partnerRepository.findById(this.partner.getPartnerId()).get();

        // then
        Assertions.assertThat(findPartner.getGender()).isEqualTo("new gender");
    }

    @Test
    @DisplayName("파트너 사진 변경")
    void changePartnerPhoto() throws IOException {

        // given
        String fileName = "profilePicture";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile partnerPhoto = getMockMultipartFile(fileName, contentType, filePath);

        MyPageDTO.PartnerInfoChangeReqDTO changePartnerDTO = MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId(String.valueOf(partner.getPartnerId()))
                .partnerPhoto(partnerPhoto)
                .build();

        // when
        partnerInfoChangeService.updatePartnerInfo(changePartnerDTO);
        PartnerPhotos changePartnerPhoto = partnerPhotosRepository.findPartnerPhotosByPartnerId(partner.getPartnerId());

        // then
        Assertions.assertThat(changePartnerPhoto.getPartnerId()).isEqualTo(partner.getPartnerId());
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}