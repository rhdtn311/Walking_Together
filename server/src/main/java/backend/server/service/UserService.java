package backend.server.service;

import backend.server.DTO.LoginDTO;
import backend.server.DTO.MailDTO;
import backend.server.DTO.UserDTO;
import backend.server.entity.Member;
import backend.server.entity.MemberRole;
import backend.server.repository.UserRepository;
import backend.server.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 회원가입
    @Transactional
    public String signup(UserDTO userDto) {
        if (userRepository.findOneWithAuthoritiesByStdId(userDto.getStdId()).orElse(null) != null) {
            return null;
        }

        if (userRepository.findMemberByEmail(userDto.getEmail()).orElse(null)!=null) {
            return "emailDup";
        }

        if (userRepository.findMemberByPhoneNumber(userDto.getPhoneNumber()).orElse(null) != null) {
            return "phoneNumberDup";
        }

        Member member = Member.builder()
                .stdId(userDto.getStdId())
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .birth(userDto.getBirth())
                .email(userDto.getEmail())
                .totalTime(0)
                .activate(true)
                .department(userDto.getDepartment())
                .phoneNumber(userDto.getPhoneNumber())
                .distance(0L)
                .build();

        member.addMemberRole(MemberRole.ROLE_USER);

        return userRepository.save(member).getStdId();
    }

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

    // 임시비밀번호로 저장
    @Transactional
    public String saveTempPassword(String email, String password) {

        String savePassword = passwordEncoder.encode(password);

        UserDTO dto = UserDTO.builder().password(savePassword).build();

        Member member = userRepository.findMemberByEmail(email).get();
        member.changePassword(dto.getPassword());

        return member.getStdId();
    }

    // 비밀번호 찾아서 메일 보내기
    @Transactional
    public String findPassword(String stdId, String name, String birth) {

        Optional<Member> findMember = userRepository.findMemberByStdIdAndNameAndBirth(stdId, name, birth);

        if(findMember.isEmpty()) {
            return "noMember";
        } else {
            String email = findMember.get().getEmail();
            String tempPassword = sendPasswordByMail(email);
            saveTempPassword(email, tempPassword);
            return email;
        }
    }

    // 이메일로 임시 비밀번호를 보냄
    public String sendPasswordByMail(String email) {

        String tempPassword = makeTempPassword();

        MailDTO sendMail = MailDTO.builder()
                .title("<< 임시 비밀번호입니다. >>")
                .message("임시 비밀번호는 " + tempPassword + " 입니다.")
                .address(email)
                .build();

        mailService.mailSend(sendMail);
        return tempPassword;    // 임시비밀번호
    }

    // 회원가입 메일 보내기
    public String sendAuthNumber(String email, String authNum) {
        Optional<Member> member = userRepository.findMemberByEmail(email);
        if(member.isPresent()) {
            return "409";
        }

        MailDTO sendAuthNum = MailDTO.builder()
                .title("<< 인증 코드입니다. >>")
                .message("인증 코드는 " + authNum + " 입니다.")
                .address(email)
                .build();

        mailService.mailSend(sendAuthNum);
        return email;
    }

    // 임시 비밀번호 생성 (7글자 무작위 단어 생성)
    public String makeTempPassword() {
        char pwCollection[] = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                };//배열에 선언

        String ranPw = "";

        for (int i = 0; i < 7; i++) {
            int selectRandomPw = (int)(Math.random()*(pwCollection.length));//Math.rondom()은 0.0이상 1.0미만의 난수를 생성해 준다.
            ranPw += pwCollection[selectRandomPw];
        }
        return ranPw;
    }


    private LoginDTO entityToDto(Member entity) {

        return LoginDTO.builder()
                .password(entity.getPassword())
                .build();
    }
}
