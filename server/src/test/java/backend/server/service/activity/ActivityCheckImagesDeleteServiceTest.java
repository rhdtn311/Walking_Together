package backend.server.service.activity;

import backend.server.entity.ActivityCheckImages;
import backend.server.repository.ActivityCheckImagesRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ActivityCheckImagesDeleteServiceTest {

    @Autowired
    ActivityCheckImagesRepository activityCheckImagesRepository;

    @Autowired
    ActivityCheckImagesDeleteService activityCheckImagesDeleteService;

    @Test
    @DisplayName("저장한 ActivityCheckImages가 제대로 삭제되는지 확인")
    void activityCheckImagesDelete() {

        // given
        ActivityCheckImages activityCheckImage1 = ActivityCheckImages.builder()
                .activityId(1L)
                .imageUrl("imageUrl1")
                .imageName("imageName1")
                .imageId(1L)
                .build();

        ActivityCheckImages activityCheckImage2 = ActivityCheckImages.builder()
                .activityId(1L)
                .imageUrl("imageUrl2")
                .imageName("imageName2")
                .imageId(1L)
                .build();

        activityCheckImagesRepository.save(activityCheckImage1);
        activityCheckImagesRepository.save(activityCheckImage2);

        // when
        activityCheckImagesDeleteService.deleteActivityCheckImages(1L);

        // then
        Assertions.assertThat(activityCheckImagesRepository.findActivityCheckImagesByActivityId(1L).get()).isEmpty();
    }

}