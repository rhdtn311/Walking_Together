package backend.server.service.feed;

import backend.server.DTO.CertificationDTO;
import backend.server.DTO.feed.FeedDetailDTO;
import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.exception.feedService.ActiveActivityNotWriteReviewException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.CertificationRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.querydsl.FeedQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final ActivityRepository activityRepository;
    private final CertificationRepository certificationRepository;
    private final MapCaptureRepository mapCaptureRepository;
    private final FeedQueryRepository feedQueryRepository;

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
