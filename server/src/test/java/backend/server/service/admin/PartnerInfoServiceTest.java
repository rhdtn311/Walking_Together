package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PartnerInfoServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerInfoService partnerInfoService;

    Member member;
    Partner ordinaryPartner;
    Partner disabledPartner;
    Partner pregnantPartner;
    Partner childPartner;

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

        userRepository.save(member);

        ordinaryPartner = Partner.builder()
                .member(member)
                .partnerName("ordinaryPartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();

        partnerRepository.save(ordinaryPartner);

        disabledPartner = Partner.builder()
                .member(member)
                .partnerName("disabledPartner")
                .selectionReason("selectionReason2")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1931-05-13")
                .partnerDetail("d")
                .build();

        partnerRepository.save(disabledPartner);

        pregnantPartner = Partner.builder()
                .member(member)
                .partnerName("pregnantPartner")
                .selectionReason("selectionReason3")
                .relationship("relationship")
                .gender("여자")
                .partnerBirth("1996-03-12")
                .partnerDetail("p")
                .build();

        partnerRepository.save(pregnantPartner);

        childPartner = Partner.builder()
                .member(member)
                .partnerName("childPartner")
                .selectionReason("selectionReason4")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("2015-03-12")
                .partnerDetail("c")
                .build();

        partnerRepository.save(childPartner);
    }

    @Test
    @DisplayName("partnerDetail로 조회되는지 확인")
    void findByMemberName() {

        // when
        AdminDTO.PartnerInfoResDTO findOrdinaryPartner = partnerInfoService.getPartnerInfo("", "o").get(0);
        AdminDTO.PartnerInfoResDTO findPregnantPartner = partnerInfoService.getPartnerInfo("", "p").get(0);
        AdminDTO.PartnerInfoResDTO findChildPartner = partnerInfoService.getPartnerInfo("", "c").get(0);

        // then
        assertThat(findOrdinaryPartner.getPartnerName()).isEqualTo("ordinaryPartner");
        assertThat(findPregnantPartner.getPartnerName()).isEqualTo("pregnantPartner");
        assertThat(findChildPartner.getPartnerName()).isEqualTo("childPartner");
    }

    @Test
    @DisplayName("keyword로 회원 학번 검색 시 조회되는지 확인")
    void findByStdIdKeyword() {
        // given
        Member anotherMember = Member.builder()
                .name("김뽀롱")
                .stdId("2015100123")
                .email("bborong123@naver.com")
                .password("password")
                .department("음악")
                .birth("19960311")
                .phoneNumber("01099991111")
                .build();

        userRepository.save(anotherMember);

        Partner anotherOrdinaryPartner = Partner.builder()
                .member(anotherMember)
                .partnerName("anotherOrdinaryPartner")
                .selectionReason("selectionReason2")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-04-13")
                .partnerDetail("o")
                .build();

        partnerRepository.save(anotherOrdinaryPartner);

        // when
        AdminDTO.PartnerInfoResDTO findOrdinaryPartner = partnerInfoService.getPartnerInfo("123", "o").get(0);

        // then
        assertThat(findOrdinaryPartner.getPartnerName()).isEqualTo("anotherOrdinaryPartner");
    }

    @Test
    @DisplayName("keyword로 회원 이름 검색 시 조회되는지 확인")
    void findByNameKeyword() {
        // given
        Member anotherMember = Member.builder()
                .name("김뽀롱")
                .stdId("2015100123")
                .email("bborong123@naver.com")
                .password("password")
                .department("음악")
                .birth("19960311")
                .phoneNumber("01099991111")
                .build();

        userRepository.save(anotherMember);

        Partner anotherOrdinaryPartner = Partner.builder()
                .member(anotherMember)
                .partnerName("anotherOrdinaryPartner")
                .selectionReason("selectionReason2")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-04-13")
                .partnerDetail("o")
                .build();

        partnerRepository.save(anotherOrdinaryPartner);

        // when
        AdminDTO.PartnerInfoResDTO findOrdinaryPartner = partnerInfoService.getPartnerInfo("김뽀롱", "o").get(0);

        // then
        assertThat(findOrdinaryPartner.getPartnerName()).isEqualTo("anotherOrdinaryPartner");
    }



}