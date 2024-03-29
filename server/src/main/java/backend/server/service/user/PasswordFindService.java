package backend.server.service.user;

import backend.server.DTO.user.MailDTO;
import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class PasswordFindService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Transactional
    public UserDTO.PasswordFindResDTO findPassword(UserDTO.PasswordFindReqDTO passwordFindReqDTO) {
        Member member = memberRepository.findMemberByStdIdAndNameAndBirth(passwordFindReqDTO.getStdId(), passwordFindReqDTO.getName(), passwordFindReqDTO.getBirth())
                .orElseThrow(MemberNotFoundException::new);

        String tempPassword = sendPasswordByMail(member.getEmail());
        member.changePassword(passwordEncoder.encode(tempPassword));

        return new UserDTO.PasswordFindResDTO(member.getEmail());
    }

    // 이메일로 임시 비밀번호를 보냄
    public String sendPasswordByMail(String email) {
        String tempPassword = makeTempPassword();

        MailDTO passwordFindDTO = MailDTO.setFindPasswordMail(email, tempPassword);

        mailService.sendMail(passwordFindDTO);

        return tempPassword;
    }

    // 임시 비밀번호 생성 (7글자 무작위 단어 생성)
    public String makeTempPassword() {
        char[] pwCollection = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
        };

        StringBuilder tempPassword = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int selectRandomPw = (int)(Math.random()*(pwCollection.length));
            tempPassword.append(pwCollection[selectRandomPw]);
        }

        return tempPassword.toString();
    }
}
