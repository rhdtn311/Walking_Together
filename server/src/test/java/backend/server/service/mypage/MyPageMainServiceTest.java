package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberProfilePictures;
import backend.server.repository.MemberProfilePicturesRepository;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MyPageMainServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberProfilePicturesRepository memberProfilePicturesRepository;

    @Autowired
    MyPageMainService myPageMainService;

    Member member;
    MemberProfilePictures memberProfilePictures;

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

        memberProfilePictures = MemberProfilePictures.builder()
                .stdId(member.getStdId())
                .profilePictureUrl("profileURL")
                .profilePictureName("profileName")
                .build();
        memberProfilePicturesRepository.save(memberProfilePictures);
    }

    @Test
    @DisplayName("회원 및 회원의 프로필 사진이 정상적으로 조회 되는지 확인")
    void findMemberAndMemberProfilePicture() {
        // when
        MyPageDTO.MyPageMainResDTO myPageMain = myPageMainService.getMyPageMain(member.getStdId());

        // then
        assertThat(myPageMain.getName()).isEqualTo(member.getName());
        assertThat(myPageMain.getTotalTime()).isEqualTo(member.getTotalTime());
        assertThat(myPageMain.getProfilePicture()).isEqualTo(memberProfilePictures.getProfilePictureUrl());
    }
}