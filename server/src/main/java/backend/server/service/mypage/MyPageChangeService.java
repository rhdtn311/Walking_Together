package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.*;
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
            if (memberProfilePicturesRepository.existsMemberProfilePicturesByStdId(myPageChangeReqDTO.getStdId())) {
                fileUploadService.updateProfilePictures(myPageChangeReqDTO.getProfilePicture(), myPageChangeReqDTO.getStdId());
            } else {
                fileUploadService.uploadProfilePictures(myPageChangeReqDTO.getProfilePicture(), myPageChangeReqDTO.getStdId());
            }
        }

        return myPageChangeReqDTO.getStdId();
    }
}
