package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.entity.Activity;
import backend.server.exception.activityService.ActivityNotFoundException;
import backend.server.repository.ActivityRepository;
import backend.server.repository.querydsl.AdminQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActivityInfoService {
    private final AdminQueryRepository adminQueryRepository;
    private final ActivityRepository activityRepository;
    // 활동정보조회
    public List<AdminDTO.ActivityInfoResDTO> getActivityInfo(AdminDTO.ActivityInfoReqDTO activityInfoReqDTO) {
        return adminQueryRepository.findActivityInfo(activityInfoReqDTO);
    }

    //활동 정보 세부 조회
    public AdminDTO.ActivityDetailInfoResDTO getActivityDetailInfo(Long activityId) {

        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isEmpty()) {
            throw new ActivityNotFoundException();
        }

        return adminQueryRepository.findActivityDetailInfo(activityId);
    }
}