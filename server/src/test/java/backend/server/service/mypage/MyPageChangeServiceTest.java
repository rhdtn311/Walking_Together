package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberProfilePictures;
import backend.server.repository.MemberProfilePicturesRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MyPageChangeServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberProfilePicturesRepository memberProfilePicturesRepository;

    @Autowired
    MyPageChangeService myPageChangeService;

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
        userRepository.save(member);
    }

    // MyPageChangeReqDTO에 비밀번호가 존재한다면 비밀번호 변경
    // MyPageChangeReqDTO에 학과가 존재한다면 학과 변경
    // MyPageChangeReqDTO에 프로필 사진이 존재한다면 프로필 사진 변경

    @Test
    @DisplayName("입력 데이터에 비밀번호가 존재한다면 비밀번호 변경 확인")
    void changePassword() {

        // given
        MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO = MyPageDTO.MyPageChangeReqDTO.builder()
                .stdId(member.getStdId())
                .password("newPassword")
                .build();

        // when
        myPageChangeService.updateMemberInfo(myPageChangeReqDTO);
        Member findMember = userRepository.findMemberByStdId(this.member.getStdId()).get();

        // then
        assertTrue(passwordEncoder.matches("newPassword", findMember.getPassword()));
    }

    @Test
    @DisplayName("입력 데이터에 학과가 존재한다면 학과 변경")
    void changeDepartment() {

        // given
        MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO = MyPageDTO.MyPageChangeReqDTO.builder()
                .stdId(member.getStdId())
                .department("음악")
                .build();

        // when
        myPageChangeService.updateMemberInfo(myPageChangeReqDTO);
        Member findMember = userRepository.findMemberByStdId(this.member.getStdId()).get();

        // then
        assertThat(findMember.getDepartment()).isEqualTo("음악");
    }

    @Test
    @DisplayName("입력 데이터에 프로필 사진이 있다면 프로필 사진 변경")
    void changeMemberProfilePicture() throws IOException {

        // given
        String fileName = "profilePicture";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile profilePicture = getMockMultipartFile(fileName, contentType, filePath);

        MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO = MyPageDTO.MyPageChangeReqDTO.builder()
                .stdId(member.getStdId())
                .profilePicture(profilePicture)
                .build();

        // when
        myPageChangeService.updateMemberInfo(myPageChangeReqDTO);
        MemberProfilePictures memberProfilePicture = memberProfilePicturesRepository.findMemberProfilePicturesByStdId(member.getStdId()).get();

        // then
        assertThat(memberProfilePicture.getStdId()).isEqualTo(member.getStdId());
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}