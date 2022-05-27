package backend.server.service.user;

import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.exception.userService.PhoneNumberDuplicationException;
import backend.server.exception.userService.StdIdDuplicationException;
import backend.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(UserDTO.SignUpReqDTO signUpReqDTO) {
        if (memberRepository.existsMemberByStdId(signUpReqDTO.getStdId())) {
            throw new StdIdDuplicationException();
        }

        if (memberRepository.existsMemberByEmail(signUpReqDTO.getEmail())) {
            throw new EmailDuplicationException();
        }

        if (memberRepository.existsMemberByPhoneNumber(signUpReqDTO.getPhoneNumber())) {
            throw new PhoneNumberDuplicationException();
        }
        Member member = signUpReqDTO.toMember(passwordEncoder);

        return memberRepository.save(member).getStdId();
    }
}
