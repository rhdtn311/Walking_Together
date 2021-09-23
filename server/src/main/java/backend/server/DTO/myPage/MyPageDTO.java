package backend.server.DTO.myPage;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class MyPageDTO {

    private static PasswordEncoder passwordEncoder;

    @Getter
    @NoArgsConstructor
    public static class MyPageMainResDTO {
        private String name;
        private String department;
        private int totalTime;
        private String profilePicture;

        @Builder
        public MyPageMainResDTO(String name, String department, int totalTime, String profilePicture) {
            this.name = name;
            this.department = department;
            this.totalTime = totalTime;
            this.profilePicture = profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyPageChangeReqDTO {
        @NotNull
        private String stdId;
        @Nullable
        private String password;
        @Nullable
        private String department;
        @Nullable
        private MultipartFile profilePicture;

        public boolean isPasswordPresent() {
            return this.getPassword() != null;
        }

        public boolean isDepartmentPresent() {
            return this.getDepartment() != null;
        }

        public boolean isProfilePicturePresent() {
            return this.getProfilePicture() != null;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyPagePartnerListResDTO {
        private String partnerName;
        private Long partnerId;
        private String partnerDetail;
        private String partnerBirth;

        @Builder
        public MyPagePartnerListResDTO(String partnerName, Long partnerId, String partnerDetail, String partnerBirth) {
            this.partnerName = partnerName;
            this.partnerId = partnerId;
            this.partnerDetail = partnerDetail;
            this.partnerBirth = partnerBirth;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyPageDetailResDTO {

    }
}
