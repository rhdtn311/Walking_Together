package backend.server.service.user;

import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberRole;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PasswordFindServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordFindService passwordFindService;

    @Test
    @DisplayName("비밀번호 찾기 정상 수행 (학번,이름,생일에 부합하는 회원 찾기)")
    void findStdIdNameBirthMember() {
        // 보류

        // given
//        Set<MemberRole> authorities = new HashSet<>();
//        authorities.add(MemberRole.ROLE_USER);
//        Member member1 = Member.builder()
//                .name("member1")
//                .birth("19960311")
//                .stdId("stdId1")
//                .password("password")
//                .department("학과1")
//                .email("email1@naver.com")
//                .phoneNumber("01011111111")
//                .distance(0L)
//                .activate(true)
//                .totalTime(0)
//                .authorities(authorities)
//                .build();
//
//        UserDTO.PasswordFindReqDTO passwordFindReqDTO = UserDTO.PasswordFindReqDTO.builder()
//                .stdId("stdId1")
//                .birth("19960311")
//                .name("member1")
//                .build();
//
//        // when
//        userRepository.save(member1);
//
//        // then
//        UserDTO.PasswordFindResDTO passwordChangedMember = passwordFindService.findPassword(passwordFindReqDTO);
//        assertThat(member1.getEmail()).isEqualTo(passwordChangedMember.getEmail());
    }

    @Test
    @DisplayName("임시 비밀번호로 회원의 비밀번호가 바뀌는지 확인")
    void changeMemberPasswordToTempPassword() {
        // 보류

        // given
//        Member member = Member.builder()
//                .name("member")
//                .birth("19960311")
//                .stdId("stdId")
//                .password("password")
//                .department("학과1")
//                .email("email2@naver.com")
//                .phoneNumber("01022222222")
//                .distance(0L)
//                .activate(true)
//                .totalTime(0)
//                .build();
//
//        userRepository.save(member);
//        // when
//        UserDTO.PasswordFindReqDTO passwordFindReqDTO = UserDTO.PasswordFindReqDTO.builder()
//                .name("member")
//                .birth("19960311")
//                .stdId("stdId")
//                .build();
//
//        passwordFindService.findPassword(passwordFindReqDTO);
//
//        // then
//        Member findMember = userRepository.findMemberByStdId("stdId").get();
//        assertThat(findMember.getPassword()).isNotEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("주어진 학번, 이름, 생일에 부합하는 회원이 없을 경우 비즈니스 예외")
    void notFoundMember() {
        // given
        Member member = Member.builder()
                .name("member")
                .birth("19960311")
                .stdId("stdId")
                .password("password")
                .department("학과1")
                .email("email2@naver.com")
                .phoneNumber("01022222222")
                .distance(0L)
                .activate(true)
                .totalTime(0)
                .build();

        userRepository.save(member);
        // when
        UserDTO.PasswordFindReqDTO nameNotSame = UserDTO.PasswordFindReqDTO.builder()
                .name("anotherMember")
                .birth("19960311")
                .stdId("stdId")
                .build();

        UserDTO.PasswordFindReqDTO birthNotSame = UserDTO.PasswordFindReqDTO.builder()
                .name("member")
                .birth("20001231")
                .stdId("stdId")
                .build();

        UserDTO.PasswordFindReqDTO stdIdNotSame = UserDTO.PasswordFindReqDTO.builder()
                .name("member")
                .birth("19960311")
                .stdId("anotherStdId")
                .build();

        // then
        assertThrows(MemberNotFoundException.class, () -> passwordFindService.findPassword(nameNotSame));
        assertThrows(MemberNotFoundException.class, () -> passwordFindService.findPassword(birthNotSame));
        assertThrows(MemberNotFoundException.class, () -> passwordFindService.findPassword(stdIdNotSame));
    }
}