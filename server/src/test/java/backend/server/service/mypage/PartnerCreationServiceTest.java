package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
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
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PartnerCreationServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    PartnerPhotoRepository partnerPhotoRepository;

    @Autowired
    PartnerCreationService partnerCreationService;

    @MockBean
    private FileUploadService fileUploadService;

    Member member;

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
    }

    @Test
    @DisplayName("주어진 조건대로 파트너 생성 확인")
    void savePartner() throws IOException {

        // given
        String fileName = "profilePicture";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile partnerPhoto = getMockMultipartFile(fileName, contentType, filePath);

        MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO = MyPageDTO.PartnerCreationReqDTO.builder()
                .stdId(member.getStdId())
                .partnerName("partnerName")
                .partnerBirth("2001-12-13")
                .selectionReason("selection Reason")
                .relationship("relationship")
                .gender("male")
                .partnerDetail("d")
                .partnerPhoto(partnerPhoto)
                .build();

        // when
        partnerCreationService.createPartner(partnerCreationReqDTO);
        Partner partner = partnerRepository.findPartnerByMember(member).get();

        // then
        assertThat(partner.getMember().getStdId()).isEqualTo(partnerCreationReqDTO.getStdId());
        assertThat(partner.getPartnerName()).isEqualTo(partnerCreationReqDTO.getPartnerName());
        assertThat(partner.getPartnerBirth().replace("/", "-")).isEqualTo(partnerCreationReqDTO.getPartnerBirth());
        assertThat(partner.getSelectionReason()).isEqualTo(partnerCreationReqDTO.getSelectionReason());
        assertThat(partner.getGender()).isEqualTo(partnerCreationReqDTO.getGender());
        assertThat(partner.getPartnerDetail()).isEqualTo(partnerCreationReqDTO.getPartnerDetail());
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}