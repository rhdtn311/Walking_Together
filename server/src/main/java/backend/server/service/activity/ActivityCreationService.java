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
import backend.server.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;
    private final ActivityQueryRepository activityQueryRepository;

    private final FileUploadService fileUploadService;

    // 활동 생성 화면
    @Transactional(readOnly = true)
    public List<PartnerDTO.PartnerListResDTO> createActivity(String stdId) {
        if (activityQueryRepository.existsActiveActivity(stdId)) {
            throw new ActivityAlreadyInProgressException();
        }

        List<Partner> partners= activityRepository.getPartnerList(stdId);

        if (partners.isEmpty()) {
            throw new PartnerNotFoundException();
        }

        return createPartnerList(partners);
    }

    public List<PartnerDTO.PartnerListResDTO> createPartnerList(List<Partner> partners) {

        return partners.stream().map(PartnerDTO.PartnerListResDTO::entityToDto).collect(Collectors.toList());
    }

    // 활동 생성 완료
    @Transactional
    public Long createActivityDone(ActivityDTO.ActivityCreationReqDTO activityCreationReq) {

        Partner partner = partnerRepository.findById(activityCreationReq.getPartnerId()).orElseThrow(PartnerNotFoundException::new);

        Member member = memberRepository.findMemberByStdId(activityCreationReq.getStdId()).orElseThrow(MemberNotFoundException::new);

        Activity activity = activityRepository.save(activityCreationReq.toActivity(member, partner));

        ActivityStartImageFileUploadDTO activityStartImageFileUploadDTO = new ActivityStartImageFileUploadDTO(activityCreationReq.getStartPhoto());
//        String fileUrl = fileUploadService.uploadFileToS3(activityStartImageFileUploadDTO);
        saveActivityStartImage(activity, "fileUrl", activityStartImageFileUploadDTO.getFileName());

        return activity.getActivityId();
    }

    public void saveActivityStartImage(Activity activity, String fileUrl, String fileName) {
        ActivityCheckImages activityStartImage = ActivityCheckImages.builder()
                .activity(activity)
                .imageUrl(fileUrl)
                .imageName(fileName)
                .build();

        activityCheckImagesRepository.save(activityStartImage);
    }
}
