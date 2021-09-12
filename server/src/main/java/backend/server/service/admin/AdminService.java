package backend.server.service.admin;

import backend.server.DTO.admin.*;
import backend.server.entity.Activity;
import backend.server.repository.ActivityRepository;
import backend.server.repository.MapCaptureRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import backend.server.repository.querydsl.AdminQueryRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
// 관리자 페이지
public class AdminService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PartnerRepository partnerRepository;
    private final MapCaptureRepository mapCaptureRepository;
    private final AdminQueryRepository adminQueryRepository;

    // 활동정보조회
    public List<ActivityInfoDTO> activityInfo(String keyword,
                                              LocalDate from,
                                              LocalDate to,
                                              int activityDivision) {
        List<Tuple> tuples = activityRepository.activityInfo(keyword, from, to, activityDivision);
        List<ActivityInfoDTO> activityList = new ArrayList<>();

        tuples.forEach(activity -> {
            ActivityInfoDTO dto = ActivityInfoDTO.builder()
                    .stdName(activity.get(0, String.class))
                    .department(activity.get(1, String.class))
                    .stdId(activity.get(2, String.class))
                    .activityDate(activity.get(3, LocalDate.class))
                    .activityStartTime(activity.get(4, LocalDateTime.class))
                    .activityId(activity.get(5, Long.class))
                    .endTime(activity.get(6, LocalDateTime.class))
                    .distance(activity.get(7, Long.class))
                    .partnerName(activity.get(8, String.class))
                    .build();

            activityList.add(dto);
        });

        return activityList;
    }

    //활동 정보 세부 조회
    public ActivityDetailInfoDTO activityDetail(Long activityId) {

        List<Tuple> tuples = activityRepository.activityDetail(activityId);
        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if(activityOptional.isEmpty()) {
            return null;
        }
        Activity activity = activityOptional.get();

        if(tuples.isEmpty()) {
            return null;
        }

        Tuple tuple = tuples.get(0);

        // member.name, member.department, member.stdId, activity.activityDate,
        //                partner.partnerName, activity.review, activity.distance, activity.startTime, activity.endTime

        ArrayList<Object> mapPicture = mapCaptureRepository.findAllByActivityId(activityId);
        ActivityDetailInfoDTO  result = ActivityDetailInfoDTO.builder()
                .stdName(tuple.get(0,String.class))
                .department(tuple.get(1,String.class))
                .stdId(tuple.get(2, String.class))
                .activityDate(tuple.get(3, LocalDate.class))
                .partnerName(tuple.get(4, String.class))
                .review(tuple.get(5, String.class))
                .totalDistance(tuple.get(6, Long.class))
                .startTime(tuple.get(7, LocalDateTime.class))
                .endTime(tuple.get(8, LocalDateTime.class))
                .totalTime(activity.getActivityDivision() == 0? activity.getOrdinaryTime():activity.getCareTime())
                .mapPicture(mapPicture)
                .build();

        return result;
    }

    // 파트너 정보 조회
    public List<PartnerInfoDTO> partnerInfo(String keyword, String partnerDetail) {

        List<Tuple> tuples = partnerRepository.partnerInfo(keyword, partnerDetail);
        List<PartnerInfoDTO> partnerList = new ArrayList<>();

        tuples.forEach(partner -> {
            //member.name, member.stdId, member.department, partner.partnerName,
            //                partner.gender, partner.partnerBirth, partner.relationship, partner.partnerDivision

            PartnerInfoDTO dto = PartnerInfoDTO.builder()
                    .stdName(partner.get(0,String.class))
                    .stdId(partner.get(1,String.class))
                    .department(partner.get(2,String.class))
                    .partnerName(partner.get(3,String.class))
                    .partnerGender(partner.get(4,String.class))
                    .partnerBirth(partner.get(5,String.class))
                    .relationship(partner.get(6,String.class))
                    .partnerDivision(partner.get(7,Integer.class))
                    .build();

            partnerList.add(dto);
        });

        return partnerList;
    }
}
