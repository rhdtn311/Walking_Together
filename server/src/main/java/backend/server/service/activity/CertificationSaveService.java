package backend.server.service.activity;

import backend.server.entity.Activity;
import backend.server.entity.Certification;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CertificationSaveService {

    private final CertificationRepository certificationRepository;

    // 인증서 생성
    @Transactional
    public Long saveCertification(Member member, Activity activity) {

        certificationRepository.save(
                Certification.builder()
                .certificationId(activity.getActivityId())
                .activityId(activity.getActivityId())
                .stdId(member.getStdId())
                .name(member.getName())
                .partnerName(activity.getPartner().getPartnerName())
                .activityDate(activity.getActivityDate())
                .department(member.getDepartment())
                .distance(activity.getDistance())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .careTime(activity.getActivityDivision() == 1 ? activity.getCareTime() : LocalTime.of(0, 0))
                .ordinaryTime(activity.getActivityDivision() == 0 ? activity.getOrdinaryTime() : LocalTime.of(0,0))
                .build()
        );

        return activity.getActivityId();
    }
}
