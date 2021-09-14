package backend.server.service;

import backend.server.DTO.CertificationDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.DTO.feed.FeedDetailDTO;
import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.entity.MapCapture;
import backend.server.repository.ActivityRepository;
import backend.server.repository.CertificationRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.UserRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final ActivityRepository activityRepository;
    private final CertificationRepository certificationRepository;
    private final MapCaptureRepository mapCaptureRepository;

    // 피드
    public List<FeedDTO> feedMain(String stdId, String sort) {

        List<Tuple> tuples = activityRepository.feed(stdId, sort);
        System.out.println(tuples);
        List<FeedDTO> result = new ArrayList<>();

        tuples.forEach( tuple -> {
            FeedDTO dto = FeedDTO.builder()
                    .activityStatus(tuple.get(0, Integer.class))
                    .distance(tuple.get(1, Long.class))
                    .partnerName(tuple.get(2, String.class))
                    .activityDate(tuple.get(3, LocalDate.class))
                    .activityDivision(tuple.get(4, Integer.class))
                    .activityId(tuple.get(5, Long.class))
                    .build();
            result.add(dto);
        });

        return result;
    }

    // 피드 상세
    public FeedDetailDTO feedDetail(Long activityId) {

        Optional<Activity> activityOptional = activityRepository.findById(activityId);

        if(activityOptional.isEmpty()) {
            return null;
        }

        Tuple result = activityRepository.feedDetail(activityId);

        List<MapCapture> mapPicture = mapCaptureRepository.findAllByActivityId(activityId);

        FeedDetailDTO dto = FeedDetailDTO.builder()
                .activityDate(result.get(0, LocalDate.class))
                .partnerName(result.get(1, String.class))
                .startTime(result.get(2, LocalDateTime.class))
                .endTime(result.get(3, LocalDateTime.class))
                .activityDivision(result.get(4, Integer.class))
                .review(result.get(5, String.class))
                .mapPicture(mapPicture)
                .build();

        return dto;
    }

    @Transactional
    public Long activityReview(Long activityId, FeedDetailDTO feedDTO) {

        Optional<Activity> activityOptional = activityRepository.findById(activityId);

        if(activityOptional.isEmpty()) {
            return 404L;
        }

        if (feedDTO.getReview() == null) {
            return 405L;
        }

        Activity activity = activityOptional.get();

        if(activity.getActivityStatus() == 1) {
            return 406L;
        }

        activity.changeReview(feedDTO.getReview());
        System.out.println(feedDTO.getReview());

        return activityId;
    }

    @Transactional
    public Map<String, Object> getCertification(String from, String to, String stdId) {

        List<Certification> certifications = certificationRepository.getCertification(from, to, stdId);
        List<CertificationDTO> dtos = new ArrayList<>();

        List<Integer> ordinaryTimes = new ArrayList<>();
        List<Integer> careTimes = new ArrayList<>();

        certifications.forEach(c -> {
            CertificationDTO certificationDTO = new CertificationDTO();
            certificationDTO.setCertificationId(c.getCertificationId());
            certificationDTO.setOrdinaryTime(c.getOrdinaryTime());
            certificationDTO.setCareTime(c.getCareTime());
            certificationDTO.setDepartment(c.getDepartment());
            certificationDTO.setActivityDate(c.getActivityDate());
            certificationDTO.setPartnerName(c.getPartnerName());
            certificationDTO.setStdId(c.getStdId());
            certificationDTO.setName(c.getName());
            certificationDTO.setStartTime(c.getStartTime());
            certificationDTO.setEndTime(c.getEndTime());
            certificationDTO.setDistance(c.getDistance());

            ordinaryTimes.add(c.getOrdinaryTime().getMinute() + (c.getOrdinaryTime().getHour() * 60));
            careTimes.add(c.getCareTime().getMinute() + (c.getCareTime().getHour() * 60));

            dtos.add(certificationDTO);
        });

        int ordinarySum = 0;
        for(int i: ordinaryTimes) {
            ordinarySum += i;
        }

        int careSum = 0;
        for(int j: careTimes) {
            careSum += j;
        }

        String ordinaryFinalTime = String.format("%02d", ordinarySum/60)+ ":" + String.format("%02d",ordinarySum%60);
        String careFinalTime = String.format("%02d",careSum/60) + ":" + String.format("%02d",careSum%60);
        String totalTime = String.format("%02d",(ordinarySum+careSum)/60) + ":" + String.format("%02d", (ordinarySum+careSum)%60);
        Map<String, Object> result  = new HashMap<>();
        result.put("data", dtos);
        result.put("ordinaryTimes", ordinaryFinalTime);
        result.put("careTimes", careFinalTime);
        result.put("totalTime", totalTime);

        return result;
    }
}
