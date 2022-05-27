package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.entity.Member;
import backend.server.repository.MemberRepository;
import backend.server.repository.querydsl.AdminQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberInfoServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AdminQueryRepository adminQueryRepository;

    static Member member1;
    static Member member2;

    @BeforeEach
    void init() {
        // given
        member1 = Member.builder()
                .name("김토토")
                .stdId("2015100885")
                .email("rhdtn311@naver.com")
                .password("password")
                .department("컴퓨터")
                .birth("19960311")
                .phoneNumber("01039281329")
                .build();

        memberRepository.save(member1);

        member2 = Member.builder()
                .name("김뽀롱")
                .stdId("2015100284")
                .email("rhdtn987@naver.com")
                .password("password")
                .department("음악")
                .birth("19961231")
                .phoneNumber("01039284132")
                .build();

        memberRepository.save(member2);
    }

    @Test
    @DisplayName("keyword가 회원의 이름인 경우 정상 조회 확인")
    void findKeywordIsName() {

        // when
        String nameKeyword1 = "김토토";
        List<AdminDTO.MemberInfoResDTO> memberInfoList1 = adminQueryRepository.findMemberInfo(nameKeyword1);
        AdminDTO.MemberInfoResDTO findMember1 = memberInfoList1.get(0);

        String nameKeyword2 = "김";
        List<AdminDTO.MemberInfoResDTO> memberInfoList2 = adminQueryRepository.findMemberInfo(nameKeyword2);

        // then
        assertThat(memberInfoList1.size()).isEqualTo(1);
        assertThat(findMember1.getName()).isEqualTo(member1.getName());
        assertThat(findMember1.getStdId()).isEqualTo(member1.getStdId());
        assertThat(findMember1.getBirth()).isEqualTo(member1.getBirth());
        assertThat(findMember1.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(findMember1.getPhoneNumber()).isEqualTo(member1.getPhoneNumber());

        assertThat(memberInfoList2.size()).isEqualTo(2);
        for (int i = 0; i < memberInfoList2.size(); i++) {
            AdminDTO.MemberInfoResDTO findMember = memberInfoList2.get(i);
            if (findMember.getName().equals("김토토")) {
                assertThat(findMember.getName()).isEqualTo(member1.getName());
                assertThat(findMember.getStdId()).isEqualTo(member1.getStdId());
                assertThat(findMember.getBirth()).isEqualTo(member1.getBirth());
                assertThat(findMember.getDepartment()).isEqualTo(member1.getDepartment());
                assertThat(findMember.getEmail()).isEqualTo(member1.getEmail());
                assertThat(findMember.getPhoneNumber()).isEqualTo(member1.getPhoneNumber());
            } else {
                assertThat(findMember.getName()).isEqualTo(member2.getName());
                assertThat(findMember.getStdId()).isEqualTo(member2.getStdId());
                assertThat(findMember.getBirth()).isEqualTo(member2.getBirth());
                assertThat(findMember.getDepartment()).isEqualTo(member2.getDepartment());
                assertThat(findMember.getEmail()).isEqualTo(member2.getEmail());
                assertThat(findMember.getPhoneNumber()).isEqualTo(member2.getPhoneNumber());
            }
        }
    }

    @Test
    @DisplayName("keyword가 회원의 학번인 경우 정상 조회 확인")
    void findKeywordIsStdId() {

        // when
        String stdIdKeyword1 = "2015100885";
        List<AdminDTO.MemberInfoResDTO> memberInfoList1 = adminQueryRepository.findMemberInfo(stdIdKeyword1);
        AdminDTO.MemberInfoResDTO findMember1 = memberInfoList1.get(0);

        String stdIdKeyword2 = "2015";
        List<AdminDTO.MemberInfoResDTO> memberInfoList2 = adminQueryRepository.findMemberInfo(stdIdKeyword2);

        // then
        assertThat(memberInfoList1.size()).isEqualTo(1);
        assertThat(findMember1.getName()).isEqualTo(member1.getName());
        assertThat(findMember1.getStdId()).isEqualTo(member1.getStdId());
        assertThat(findMember1.getBirth()).isEqualTo(member1.getBirth());
        assertThat(findMember1.getDepartment()).isEqualTo(member1.getDepartment());
        assertThat(findMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(findMember1.getPhoneNumber()).isEqualTo(member1.getPhoneNumber());

        assertThat(memberInfoList2.size()).isEqualTo(2);
        for (int i = 0; i < memberInfoList2.size(); i++) {
            AdminDTO.MemberInfoResDTO findMember = memberInfoList2.get(i);
            if (findMember.getStdId().equals("2015100885")) {
                assertThat(findMember.getName()).isEqualTo(member1.getName());
                assertThat(findMember.getStdId()).isEqualTo(member1.getStdId());
                assertThat(findMember.getBirth()).isEqualTo(member1.getBirth());
                assertThat(findMember.getDepartment()).isEqualTo(member1.getDepartment());
                assertThat(findMember.getEmail()).isEqualTo(member1.getEmail());
                assertThat(findMember.getPhoneNumber()).isEqualTo(member1.getPhoneNumber());
            } else {
                assertThat(findMember.getName()).isEqualTo(member2.getName());
                assertThat(findMember.getStdId()).isEqualTo(member2.getStdId());
                assertThat(findMember.getBirth()).isEqualTo(member2.getBirth());
                assertThat(findMember.getDepartment()).isEqualTo(member2.getDepartment());
                assertThat(findMember.getEmail()).isEqualTo(member2.getEmail());
                assertThat(findMember.getPhoneNumber()).isEqualTo(member2.getPhoneNumber());
            }
        }
    }
}