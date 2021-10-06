package backend.server.service.activity;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.activity.PartnerDTO;
import backend.server.DTO.s3.fileUpload.ActivityStartImageFileUploadDTO;
import backend.server.entity.Activity;
import backend.server.entity.ActivityCheckImages;
import backend.server.entity.Member;
import backend.server.entity.Partner;
import backend.server.exception.activityService.ActivityAlreadyInProgressException;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.exception.activityService.PartnerNotFoundException;
import backend.server.repository.ActivityCheckImagesRepository;
import backend.server.repository.ActivityRepository;
import backend.server.repository.PartnerRepository;
import backend.server.repository.UserRepository;
import backend.server.repository.querydsl.ActivityQueryRepository;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActivityCreationService {

    private final ActivityRepository activityRepository;
    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;
    private final ActivityQueryRepository activityQueryRepository;

    private final FileUploadService fileUploadService;

    // 활동 생성 화면
    @Transactional(readOnly = true)
    public List<PartnerDTO.PartnerListRes> createActivity(String stdId) {
        if (activityQueryRepository.existsActiveActivity(stdId)) {
            throw new ActivityAlreadyInProgressException();
        }

        List<Partner> partners= activityRepository.getPartnerList(stdId);

        if (partners.isEmpty()) {
            throw new PartnerNotFoundException();
        }

        return createPartnerList(partners);
    }

    public List<PartnerDTO.PartnerListRes> createPartnerList(List<Partner> partners) {

        return partners.stream().map(Partner::toPartnerListResDTO).collect(Collectors.toList());
    }

    // 활동 생성 완료
    @Transactional
    public Long createActivityDone(ActivityDTO.ActivityCreationReq activityCreationReq) {
        Optional<Partner> partnerOpt = partnerRepository.findById(activityCreationReq.getPartnerId());
        if (partnerOpt.isEmpty()) {
            throw new PartnerNotFoundException();
        }

        Optional<Member> memberOpt = userRepository.findMemberByStdId(activityCreationReq.getStdId());
        if (memberOpt.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Partner partner = partnerOpt.get();
        Member member = memberOpt.get();

        Activity activity = activityRepository.save(activityCreationReq.toEntity(member, partner));

        ActivityStartImageFileUploadDTO activityStartImageFileUploadDTO = new ActivityStartImageFileUploadDTO(activityCreationReq.getStartPhoto());
        String fileUrl = fileUploadService.uploadFileToS3(activityStartImageFileUploadDTO);
        saveActivityStartImage(activity.getActivityId(), fileUrl, activityStartImageFileUploadDTO.getFileName());

        return activity.getActivityId();
    }

    public void saveActivityStartImage(Long activityId, String fileUrl, String fileName) {
        ActivityCheckImages activityStartImage = ActivityCheckImages.builder()
                .activityId(activityId)
                .imageUrl(fileUrl)
                .imageName(fileName)
                .build();

        activityCheckImagesRepository.save(activityStartImage);
    }
}
