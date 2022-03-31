package backend.server.service.feed;

import backend.server.DTO.feed.FeedDTO;
import backend.server.entity.Activity;
import backend.server.entity.Member;
import backend.server.entity.Partner;
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
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FeedMainServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FeedMainService feedMainService;

    Member member;
    Partner ordinaryPartner;
    Partner disabledPartner;
    Activity activity1;
    Activity activity2;
    Activity activity3;
    Activity[] activities;

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

        ordinaryPartner = Partner.builder()
                .member(member)
                .partnerName("ordinaryPartner")
                .selectionReason("selectionReason1")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1996-03-12")
                .partnerDetail("o")
                .build();
        partnerRepository.save(ordinaryPartner);

        disabledPartner = Partner.builder()
                .member(member)
                .partnerName("disabledPartner")
                .selectionReason("selectionReason2")
                .relationship("relationship")
                .gender("남자")
                .partnerBirth("1931-05-13")
                .partnerDetail("d")
                .build();
        partnerRepository.save(disabledPartner);

        activity1 = Activity.builder()
                .member(member)
                .distance(5000L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2022, 3, 1))
                .build();
        activityRepository.save(activity1);

        activity2 = Activity.builder()
                .member(member)
                .distance(4420L)
                .activityDivision(0)
                .activityDate(LocalDate.of(2021, 12, 31))
                .build();
        activityRepository.save(activity2);

        activity3 = Activity.builder()
                .member(member)
                .distance(3422L)
                .activityDivision(1)
                .activityDate(LocalDate.of(2022, 1, 31))
                .build();
        activityRepository.save(activity3);

        activities = new Activity[]{activity1, activity2, activity3};
    }

    @Test
    @DisplayName("학번으로 활동 리스트 출력")
    void findByStdId() {

        // when
        List<FeedDTO.FeedMainResDTO> feedList = feedMainService.getFeedMain("2015100885", "desc");

        // then
        assertThat(feedList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("활동 날짜로 오름차순, 내림차순 확인")
    void checkOrder() {

        // when
        List<FeedDTO.FeedMainResDTO> descFeedList = feedMainService.getFeedMain("2015100885", "desc");
        List<FeedDTO.FeedMainResDTO> ascFeedList = feedMainService.getFeedMain("2015100885", "acs");

        int[] ascArr = new int[]{1, 2, 0};
        int[] descArr = new int[]{0, 2, 1};

        // then
        // asc : activity 2 -> 3 -> 1, desc : activity 1 -> 3 -> 2
        for (int i = 0; i < descFeedList.size(); i++) {
            assertThat(descFeedList.get(i).getActivityDate()).isEqualTo(activities[descArr[i]].getActivityDate());
            assertThat(ascFeedList.get(i).getActivityDate()).isEqualTo(activities[ascArr[i]].getActivityDate());
        }
    }
}