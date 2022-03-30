package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberProfilePictures;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.MemberProfilePicturesRepository;
import backend.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageMainService {

    private final UserRepository userRepository;
    private final MemberProfilePicturesRepository memberProfilePicturesRepository;

    // 마이페이지 메인
    @Transactional(readOnly = true)
    public MyPageDTO.MyPageMainResDTO getMyPageMain(String stdId) {
        Optional<Member> memberOptional = userRepository.findMemberByStdId(stdId);
        if(memberOptional.isEmpty()) {
            throw new MemberNotFoundException();
        }
        Member member = memberOptional.get();
        MyPageDTO.MyPageMainResDTO myPageMainResDTO = MyPageDTO.MyPageMainResDTO.entityToDto(member);

        Optional<MemberProfilePictures> profilePictureOptional = memberProfilePicturesRepository.findMemberProfilePicturesByStdId(stdId);
        myPageMainResDTO.setProfilePicture(profilePictureOptional.isEmpty() ? null : profilePictureOptional.get().getProfilePictureUrl());

        return myPageMainResDTO;
    }
}
