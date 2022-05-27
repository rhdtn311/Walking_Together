package backend.server.service.user;

import backend.server.DTO.user.MailDTO;
import backend.server.DTO.user.UserDTO;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerificationNumberSendService {

    private final MemberRepository memberRepository;
    private final MailService mailService;

    public UserDTO.VerificationNumberSendResDTO sendVerificationNumber(String email) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new EmailDuplicationException();
        }

        MailDTO signUpMailDTO = MailDTO.setSignUpVerification(email);

        mailService.sendMail(signUpMailDTO);

        return new UserDTO.VerificationNumberSendResDTO(signUpMailDTO.getMessage().substring(7,14));
    }
}
