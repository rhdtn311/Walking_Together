package backend.server.service.user;

import backend.server.DTO.LoginDTO;
import backend.server.DTO.MailDTO;
import backend.server.DTO.user.UserDTO;
import backend.server.entity.Member;
import backend.server.exception.activityService.MemberNotFoundException;
import backend.server.exception.userService.EmailDuplicationException;
import backend.server.repository.UserRepository;
import backend.server.security.util.SecurityUtil;
import backend.server.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.Math.abs;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // stdId 에 해당하는 user 객체와 권한 정보를 가져옴
    @Transactional(readOnly = true)
    public Optional<Member> getUserWithAuthorities(String stdId) {
        return userRepository.findOneWithAuthoritiesByStdId(stdId);
    }

    // 현재 Security Context에 저장되어 있는 username에 해당하는 user정보와 권한 정보만 가져옴
    @Transactional(readOnly = true)
    public Optional<Member> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByStdId);
    }
}
