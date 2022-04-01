package backend.server.service.ranking;

import backend.server.DTO.ranking.RankingDTO;
import backend.server.entity.Member;
import backend.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RankingServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RankingService rankingService;


    @Test
    @DisplayName("거리 순으로 랭킹 확인")
    void ranking() {
        // given
        Member memberA = Member.builder()
                .stdId("stdIdA")
                .name("memberA")
                .email("emailA")
                .phoneNumber("phoneNumberA")
                .password("password")
                .distance(100L)
                .build();
        userRepository.save(memberA);

        Member memberB = Member.builder()
                .stdId("stdIdB")
                .name("memberB")
                .email("emailB")
                .phoneNumber("phoneNumberB")
                .password("password")
                .distance(4000L)
                .build();
        userRepository.save(memberB);

        Member memberC = Member.builder()
                .stdId("stdIdC")
                .name("memberC")
                .email("emailC")
                .phoneNumber("phoneNumberC")
                .password("password")
                .distance(4512L)
                .build();
        userRepository.save(memberC);

        Member memberD = Member.builder()
                .stdId("stdIdD")
                .name("memberD")
                .email("emailD")
                .phoneNumber("phoneNumberD")
                .password("password")
                .distance(0L)
                .build();
        userRepository.save(memberD);

        Member memberE = Member.builder()
                .stdId("stdIdE")
                .name("memberE")
                .email("emailE")
                .phoneNumber("phoneNumberE")
                .password("password")
                .distance(3510L)
                .build();
        userRepository.save(memberE);

        Member memberF = Member.builder()
                .stdId("stdIdF")
                .name("memberF")
                .email("emailF")
                .phoneNumber("phoneNumberF")
                .password("password")
                .distance(5021L)
                .build();
        userRepository.save(memberF);

        Member memberG = Member.builder()
                .stdId("stdIdG")
                .name("memberG")
                .email("emailG")
                .phoneNumber("phoneNumberG")
                .password("password")
                .distance(1000L)
                .build();
        userRepository.save(memberG);

        Member memberH = Member.builder()
                .stdId("stdIdH")
                .name("memberH")
                .email("emailH")
                .phoneNumber("phoneNumberH")
                .password("password")
                .distance(9021L)
                .build();
        userRepository.save(memberH);

        Member memberI = Member.builder()
                .stdId("stdIdI")
                .name("memberI")
                .email("emailI")
                .phoneNumber("phoneNumberI")
                .password("password")
                .distance(1051L)
                .build();
        userRepository.save(memberI);

        Member memberJ = Member.builder()
                .stdId("stdIdJ")
                .name("memberJ")
                .email("emailJ")
                .phoneNumber("phoneNumberJ")
                .password("password")
                .distance(5121L)
                .build();
        userRepository.save(memberJ);

        Member memberK = Member.builder()
                .stdId("stdIdK")
                .name("memberK")
                .email("emailK")
                .phoneNumber("phoneNumberK")
                .password("password")
                .distance(105L)
                .build();
        userRepository.save(memberK);

        Member[] rankingArr = new Member[]{memberH, memberJ, memberF, memberC, memberB, memberE, memberI, memberG, memberK, memberA};

        // when
        List<RankingDTO> ranking = rankingService.getRanking();

        // then
        for (int i = 0; i < 10; i++) {
            assertThat(ranking.get(i).getName()).isEqualTo(rankingArr[i].getName());
        }
    }

}