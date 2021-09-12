package backend.server.DTO.admin;

import lombok.*;

public class AdminDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberResDTO {
        private String name;
        private String stdId;
        private String department;
        private String email;
        private String birth;
        private String phoneNumber;

        @Builder
        public MemberResDTO(String name, String stdId, String department, String email, String birth, String phoneNumber) {
            this.name = name;
            this.stdId = stdId;
            this.department = department;
            this.email = email;
            this.birth = birth;
            this.phoneNumber = phoneNumber;
        }
    }
}
