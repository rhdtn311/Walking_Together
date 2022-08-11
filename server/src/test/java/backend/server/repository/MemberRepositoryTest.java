package backend.server.repository;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void init() {

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member member = memberRepository.save(Member.builder()
                    .stdId("stdId" + i)
                    .name("name" + i)
                    .email("email" + i)
                    .password("password" + i)
                    .birth("birth" + i)
                    .department("department" + i)
                    .phoneNumber("phone_number" + i)
                    .distance((long) 1000)
                    .totalTime(60)
                    .profilePicture("profilePicture" + i)
                    .activate(true)
                    .build());
            members.add(member);
        }

        List<Partner> partners = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Partner partner = Partner.builder()
                    .partnerName("partnerName" + i)
                    .member(members.get(i)).build();
            partnerRepository.save(partner);
            partners.add(partner);
        }

        for (int i = 0; i < 5; i++) {
            activityRepository.save(
                    Activity.builder()
                            .member(members.get(i))
                            .partner(partners.get(i))
                            .build()
            );
        }
    }

    @Test
    @DisplayName("학번, 이름, 생일로 회원 찾기")
    void findMemberByStdIdAndNameAndBirth() {
        // given
        String stdId = "stdId0";
        String name = "name0";
        String birth = "birth0";

        // when
        Member member = memberRepository.findMemberByStdIdAndNameAndBirth(stdId, name, birth).get();

        // then
        assertThat(member.getStdId()).isEqualTo(stdId);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getBirth()).isEqualTo(birth);
    }

    @Test
    @DisplayName("학번으로 조회")
    void test() {
        entityManager.clear();
        System.out.println("-------------------------");
        System.out.println("--------------------------");
    }

}