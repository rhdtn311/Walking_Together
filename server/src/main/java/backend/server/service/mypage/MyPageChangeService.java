package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.DTO.s3.fileUpload.MemberProfileImageFileUploadDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberProfilePictures;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.*;
import backend.server.s3.FileUpdateService;
import backend.server.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageChangeService {

    private final UserRepository userRepository;
    private final MemberProfilePicturesRepository memberProfilePicturesRepository;

    private final FileUploadService fileUploadService;
    private final FileUpdateService fileUpdateService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String changeMemberInfo(MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO) {
        Optional<Member> memberOptional = userRepository.findMemberByStdId(myPageChangeReqDTO.getStdId());
        if (memberOptional.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Member member = memberOptional.get();
        if (myPageChangeReqDTO.isPasswordPresent()) {
            member.changePassword(passwordEncoder.encode(myPageChangeReqDTO.getPassword()));
        }

        if (myPageChangeReqDTO.isDepartmentPresent()) {
            member.changeDepartment(myPageChangeReqDTO.getDepartment());
        }

        if (myPageChangeReqDTO.isProfilePicturePresent()) {
            MemberProfileImageFileUploadDTO memberProfileImageFileUploadDTO = new MemberProfileImageFileUploadDTO(myPageChangeReqDTO.getProfilePicture());
            if (memberProfilePicturesRepository.existsMemberProfilePicturesByStdId(myPageChangeReqDTO.getStdId())) {
                String fileUrl = fileUpdateService.updateFile(memberProfileImageFileUploadDTO, memberProfilePicturesRepository, myPageChangeReqDTO.getStdId());
                saveMemberProfileImage(fileUrl, memberProfileImageFileUploadDTO.getFileName(), myPageChangeReqDTO.getStdId());
            } else {
                String fileUrl = fileUploadService.uploadFileToS3(memberProfileImageFileUploadDTO);
                saveMemberProfileImage(fileUrl, memberProfileImageFileUploadDTO.getFileName(), myPageChangeReqDTO.getStdId());
            }
        }

        return myPageChangeReqDTO.getStdId();
    }

    private void saveMemberProfileImage(String fileUrl, String fileName, String stdId) {
        if (memberProfilePicturesRepository.existsMemberProfilePicturesByStdId(stdId)) {
            MemberProfilePictures memberProfilePictures = memberProfilePicturesRepository.findMemberProfilePicturesByStdId(stdId).get();
            memberProfilePictures.changeFileUrl(fileUrl);
            memberProfilePictures.changeFileName(fileName);
        } else {
            MemberProfilePictures memberProfilePicture = MemberProfilePictures.builder()
                    .profilePictureName(fileName)
                    .profilePictureUrl(fileUrl)
                    .stdId(stdId)
                    .build();
            memberProfilePicturesRepository.save(memberProfilePicture);
        }
    }
}
