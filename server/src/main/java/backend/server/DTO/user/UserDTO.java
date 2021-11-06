package backend.server.DTO.user;

import backend.server.entity.Member;
import backend.server.entity.MemberRole;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReqDTO {
        private String stdId;
        private String password;
        private String name;
        private String email;
        private String phoneNumber;
        private String birth;
        private String department;

        public Member signUpReqDTOToMember(PasswordEncoder passwordEncoder) {
            Member member = Member.builder()
                    .stdId(this.getStdId())
                    .name(this.getName())
                    .password(passwordEncoder.encode(this.getPassword()))
                    .birth(this.getBirth())
                    .email(this.getEmail())
                    .department(this.getDepartment())
                    .phoneNumber(this.getPhoneNumber())
                    .totalTime(0)
                    .activate(true)
                    .distance(0L)
                    .build();

            member.addMemberRole(MemberRole.ROLE_USER);

            return member;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class VerificationNumberSendResDTO {
        private String authNum;

        @Builder
        public VerificationNumberSendResDTO(String authNum) {
            this.authNum = authNum;
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PasswordFindReqDTO {
        private String birth;
        private String name;
        private String stdId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PasswordFindResDTO {
        private String email;

        @Builder
        public PasswordFindResDTO(String email) {
            this.email = email;
        }
    }
}
