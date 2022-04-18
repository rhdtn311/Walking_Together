package backend.server.service.user;

import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberRole;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.exception.userService.PhoneNumberDuplicationException;
import backend.server.exception.userService.StdIdDuplicationException;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SignUpServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SignUpService signUpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 테스트")
    void signup() {
        // given
        UserDTO.SignUpReqDTO signUpReqDTO = UserDTO.SignUpReqDTO.builder()
                .stdId("2015100885")
                .name("member")
                .email("email@email.com")
                .phoneNumber("01012345678")
                .birth("19960311")
                .password("password1234")
                .department("테스트학과")
                .build();

        // when
        signUpService.signup(signUpReqDTO);

        Member findMember = userRepository.findMemberByStdId("2015100885").get();

        // then
        assertThat(signUpReqDTO.getStdId()).isEqualTo(findMember.getStdId());
        assertThat(signUpReqDTO.getName()).isEqualTo(findMember.getName());
        assertThat(signUpReqDTO.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(signUpReqDTO.getPhoneNumber()).isEqualTo(findMember.getPhoneNumber());
        assertThat(signUpReqDTO.getBirth()).isEqualTo(findMember.getBirth());
        assertThat(signUpReqDTO.getDepartment()).isEqualTo(findMember.getDepartment());

        assertThat(findMember.getTotalTime()).isEqualTo(0);
        assertThat(findMember.getDistance()).isEqualTo(0L);

        assertTrue(passwordEncoder.matches(signUpReqDTO.getPassword(), findMember.getPassword()));
        assertTrue(findMember.isActivate());
        assertTrue(findMember.getAuthorities().contains(MemberRole.ROLE_USER));
        assertFalse(findMember.getAuthorities().contains(MemberRole.ROLE_ADMIN));

        assertNull(findMember.getPartners());
        assertNull(findMember.getActivities());
        assertNull(findMember.getProfilePicture());
    }

    @Test
    @DisplayName("회원가입시 비밀번호 정상 인코딩 테스트")
    void passwordEncoding() {
        // given
        UserDTO.SignUpReqDTO signUpReqDTO = UserDTO.SignUpReqDTO.builder()
                .stdId("11111")
                .name("member")
                .email("email@email.com")
                .phoneNumber("01012345678")
                .birth("19960311")
                .password("password1234")
                .department("테스트학과")
                .build();

        signUpService.signup(signUpReqDTO);

        // when
        Member findMember = userRepository.findMemberByStdId("11111").get();

        // then
        assertTrue(passwordEncoder.matches(signUpReqDTO.getPassword(), findMember.getPassword()));
    }

    @Test
    @DisplayName("회원가입시 학번 중복 체크")
    void stdIdDuplicate() {
        // given
        UserDTO.SignUpReqDTO signUpReqDTO1 = UserDTO.SignUpReqDTO.builder()
                .stdId("11111")
                .name("member1")
                .email("email1@email.com")
                .phoneNumber("01012345678")
                .birth("19960311")
                .password("password1234")
                .department("테스트학과1")
                .build();

        signUpService.signup(signUpReqDTO1);

        // when
        UserDTO.SignUpReqDTO signUpReqDTO2 = UserDTO.SignUpReqDTO.builder()
                .stdId("11111")
                .name("member2")
                .email("email2@email.com")
                .phoneNumber("01011112222")
                .birth("19960412")
                .password("password12344")
                .department("테스트학과2")
                .build();

        // then
        assertThrows(StdIdDuplicationException.class, () -> {
            signUpService.signup(signUpReqDTO2);
        });
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복 체크")
    void emailDuplicate() {
        // given
        UserDTO.SignUpReqDTO signUpReqDTO1 = UserDTO.SignUpReqDTO.builder()
                .stdId("stdId1")
                .name("member1")
                .email("email@naver.com")
                .phoneNumber("01012345678")
                .birth("19960311")
                .password("password1234")
                .department("테스트학과1")
                .build();

        signUpService.signup(signUpReqDTO1);

        // when
        UserDTO.SignUpReqDTO signUpReqDTO2 = UserDTO.SignUpReqDTO.builder()
                .stdId("stdId2")
                .name("member2")
                .email("email@naver.com")
                .phoneNumber("01011112222")
                .birth("19960412")
                .password("password123456")
                .department("테스트학과2")
                .build();

        // then
        assertThrows(EmailDuplicationException.class, () -> signUpService.signup(signUpReqDTO2));
    }

    @Test
    @DisplayName("회원가입 시 휴대폰 번호 중복 체크")
    void phoneNumberDuplicate() {
        // given
        UserDTO.SignUpReqDTO signUpReqDTO1 = UserDTO.SignUpReqDTO.builder()
                .stdId("stdId1")
                .name("member1")
                .email("email1@naver.com")
                .phoneNumber("01012345678")
                .birth("19960311")
                .password("password1234")
                .department("테스트학과1")
                .build();

        signUpService.signup(signUpReqDTO1);

        // when
        UserDTO.SignUpReqDTO signUpReqDTO2 = UserDTO.SignUpReqDTO.builder()
                .stdId("stdId2")
                .name("member2")
                .email("email2@naver.com")
                .phoneNumber("01012345678")
                .birth("19960412")
                .password("password123456")
                .department("테스트학과2")
                .build();

        // then
        assertThrows(PhoneNumberDuplicationException.class, () -> signUpService.signup(signUpReqDTO2));

    }

}