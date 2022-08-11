package backend.server.repository;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@Import(TestConfig.class)
@DataJpaTest
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private MemberRepository memberRepository;

    Member member;
    List<Partner> partners = new ArrayList<>();
    Activity activity;

    @BeforeEach
    void init() {
        member = Member.builder()
                .name("member")
                .stdId("2015100885")
                .email("rhdtn311@naver.com")
                .password("password")
                .department("컴퓨터")
                .birth("19960311")
                .phoneNumber("01039281329")
                .build();
        memberRepository.save(member);

        for (int i = 0; i < 5; i++) {
            Partner partner = Partner.builder()
                    .member(member)
                    .partnerName("partnerName" + i)
                    .selectionReason("selectionReason" + i)
                    .relationship("relationship" + i)
                    .gender("gender" + i)
                    .partnerBirth("1996-03-1" + i)
                    .partnerDetail("o")
                    .build();
            partnerRepository.save(partner);
            partners.add(partner);
        }

        activity = Activity.builder()
                .member(member)
                .partner(partners.get(0))
                .build();
        activityRepository.save(activity);
    }

    @Test
    void getPartnerListTest() {
        // given
        String stdId = member.getStdId();

        // when
        List<Partner> findPartners = activityRepository.getPartnerList(stdId);

        // then
        for (int i = 0; i < 5; i++) {
            Partner findPartner = findPartners.get(i);
            Partner savedPartner = partners.get(i);

            System.out.println("findPartner : " + findPartner);
            System.out.println("savedPartner : " + savedPartner);

            assertThat(findPartner).isEqualTo(savedPartner);
        }
    }
}
