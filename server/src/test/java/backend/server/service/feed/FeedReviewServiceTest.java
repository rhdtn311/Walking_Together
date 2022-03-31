package backend.server.service.feed;

import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.feedService.ActiveActivityNotWriteReviewException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FeedReviewServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FeedReviewService feedReviewService;

    Member member;
    Partner partner;
    Activity doneActivity;
    Activity activeActivity;

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

        partner = Partner.builder()
                .member(member)
                .partnerName("ordinaryPartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();

        partnerRepository.save(partner);

        doneActivity = Activity.builder()
                .member(member)
                .distance(5000L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 3, 1))
                .activityStatus(0)
                .build();

        activityRepository.save(doneActivity);

        activeActivity = Activity.builder()
                .member(member)
                .distance(4420L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2021, 12, 31))
                .activityStatus(1)
                .build();

        activityRepository.save(activeActivity);
    }

    @Test
    @DisplayName("리뷰가 정상적으로 작성되는지 확인")
    void createReview() {

        // when
        feedReviewService.writeActivityReview(doneActivity.getActivityId(), "좋은 경험");

        // then
        assertThat(doneActivity.getReview()).isEqualTo("좋은 경험");
    }

    @Test
    @DisplayName("활동이 현재 진행 중이 상황일 때 ActiveActivityNotWriteReviewException 확인")
    void throwActiveActivityNotWriteReviewException() {

        // then
        assertThrows(ActiveActivityNotWriteReviewException.class, () -> {
            feedReviewService.writeActivityReview(activeActivity.getActivityId(), "좋은 경험");
        });
    }
}