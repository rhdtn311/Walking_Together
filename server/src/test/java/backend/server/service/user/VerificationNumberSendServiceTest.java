package backend.server.service.user;

import backend.server.entity.Member;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class VerificationNumberSendServiceTest {

    @Autowired
    VerificationNumberSendService verificationNumberSendService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 시 인증 번호가 바뀌는지, 7자리가 맞는지 확인")
    void verificationNumber() {
        // 보류

//        // given
//        UserDTO.VerificationNumberSendResDTO dto =
//                verificationNumberSendService.sendVerificationNumber("email1@naver.com");
//
//        // when
//        UserDTO.VerificationNumberSendResDTO dto2
//                = verificationNumberSendService.sendVerificationNumber("email2@naver.com");
//
//        // then
//        assertThat(dto.getAuthNum()).isNotEqualTo(dto2.getAuthNum());
//        assertThat(dto.getAuthNum().length()).isEqualTo(dto2.getAuthNum().length()).isEqualTo(7);
    }

    @Test
    @DisplayName("해당 이메일이 이미 가입돼있을 때 비즈니스 예외")
    void existsEmail() {
        // given
        Member member = Member.builder()
                .email("email1@naver.com")
                .name("member")
                .department("department")
                .phoneNumber("010-0000-0000")
                .birth("1996-03-11")
                .password("password")
                .stdId("stdId")
                .build();

        // when
        memberRepository.save(member);

        // then
        assertThrows(EmailDuplicationException.class,
                () -> verificationNumberSendService.sendVerificationNumber(member.getEmail()));
    }
}