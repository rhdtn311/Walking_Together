package backend.server.controller;

import backend.server.DTO.FindPasswordDTO;
import backend.server.DTO.user.UserDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.entity.Member;
import backend.server.exception.ApiException;
import backend.server.message.Message;
import backend.server.service.user.PasswordFineService;
import backend.server.service.user.SignUpService;
import backend.server.service.user.UserService;
import backend.server.service.user.VerificationNumberSendService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final SignUpService signUpService;
    private final VerificationNumberSendService verificationNumberSendService;
    private final PasswordFineService passwordFineService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody UserDTO.SignUpReqDTO signUpReqDTO) {
        String stdId = signUpService.signup(signUpReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("회원가입이 성공적으로 완료되었습니다.")
                .data(stdId)
                .build());
    }

    // 회원가입시 인증코드 전송
    @GetMapping("/signup/authNum") // {}로 바꾸기
    public ResponseEntity<ResponseDTO> sendVerificationNumber(@RequestParam(value = "email", required = false) String email) {
        UserDTO.VerificationNumberSendResDTO verificationNumberSendResDTO
                = verificationNumberSendService.sendVerificationNumber(email);

        return ResponseEntity.ok(ResponseDTO.builder()
                    .message("메일이 성공적으로 발송되었습니다.")
                    .data(verificationNumberSendResDTO)
                    .build());
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Member> getMyUserInfo() {

        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Member> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }

    // 비밀번호 찾기
    @PostMapping("/findpassword")
    public ResponseEntity<ResponseDTO> findPassword(@RequestBody UserDTO.PasswordFindReqDTO passwordFindReqDTO) {
        UserDTO.PasswordFindResDTO passwordFindResDTO = passwordFineService.findPassword(passwordFindReqDTO);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("임시 비밀번호가 입력하신 이메일로 발송 되었습니다.")
                .data(passwordFindResDTO)
                .build());
    }

    @GetMapping("/authenticTest")
    public String authenticTest(@RequestBody String str) {

        return str;
    }
}
