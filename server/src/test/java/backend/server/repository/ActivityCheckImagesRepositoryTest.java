package backend.server.repository;

import backend.TestConfig;
import backend.server.entity.ActivityCheckImages;
import backend.server.repository.querydsl.AdminQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({TestConfig.class, AdminQueryRepository.class})
@DataJpaTest
class ActivityCheckImagesRepositoryTest {

    @Autowired
    private ActivityCheckImagesRepository activityCheckImagesRepository;

    @BeforeEach
    void init() {
        for (int i = 0; i < 5; i++) {
            activityCheckImagesRepository.save(
                    ActivityCheckImages.builder()
                    .imageName("imageName" + i)
                    .imageUrl("imageUrl"+i)
                    .build()
            );
        }
    }

    @Test
    void findTest() {
        // given
        Long activityId = 1L;

        // when
        activityCheckImagesRepository.findImagesByActivityId(activityId);

        // then

    }

}