package backend.server.service.user;

import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.exception.userService.PhoneNumberDuplicationException;
import backend.server.exception.userService.StdIdDuplicationException;
import backend.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(UserDTO.SignUpReqDTO signUpReqDTO) {
        if (userRepository.existsMemberByStdId(signUpReqDTO.getStdId())) {
            throw new StdIdDuplicationException();
        }

        if (userRepository.existsMemberByEmail(signUpReqDTO.getEmail())) {
            throw new EmailDuplicationException();
        }

        if (userRepository.existsMemberByPhoneNumber(signUpReqDTO.getPhoneNumber())) {
            throw new PhoneNumberDuplicationException();
        }
        Member member = signUpReqDTO.toMember(passwordEncoder);

        return userRepository.save(member).getStdId();
    }
}
