package backend.server.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

import static java.lang.Math.abs;

@Builder
@Getter
@Setter
@Data
public class MailDTO {

    private String address; // 수신자 이메일
    private String title;   // 제목
    private String message; // 내용

    public static MailDTO setSignUpVerification(String email) {
        return MailDTO.builder()
                .title("<< WalkingTogether 회원가입 인증 코드입니다. >>")
                .message("인증 코드는 " + makeVerificationNumber() + "입니다.")
                .address(email)
                .build();
    }

    private static String makeVerificationNumber() {
        Random random = new Random();
        return String.valueOf(abs(random.nextInt())).substring(0, 7);
    }

    public static MailDTO setFindPasswordMail(String email, String tempPassword) {
        return MailDTO.builder()
                .title("<< WalkingTogether 임시 비밀번호입니다. >>")
                .message("임시 비밀번호는 " + tempPassword + " 입니다.")
                .address(email)
                .build();
    }
}
