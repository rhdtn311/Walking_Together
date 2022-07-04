package backend.server.DTO.myPage;

import backend.server.entity.Member;
import backend.server.entity.Partner;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class MyPageDTO {

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

        public static MyPageMainResDTO entityToDto(Member member) {
            return MyPageMainResDTO.builder()
                    .name(member.getName())
                    .department(member.getDepartment())
                    .totalTime(member.getTotalTime())
                    .build();
        }
    }

    @Builder
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
    public static class MyPagePartnerDetailResDTO {
        private String partnerName;
        private String partnerDetail;
        private String partnerBirth;
        private String gender;
        private String selectionReason;
        private String relationship;

        @Builder
        public MyPagePartnerDetailResDTO(String partnerName, String partnerDetail, String partnerBirth,
                                         String gender, String selectionReason, String relationship) {
            this.partnerName = partnerName;
            this.partnerDetail = partnerDetail;
            this.partnerBirth = partnerBirth;
            this.gender = gender;
            this.selectionReason = selectionReason;
            this.relationship = relationship;
        }

        public static MyPagePartnerDetailResDTO entityToDTO(Partner partner) {
            return MyPagePartnerDetailResDTO
                    .builder()
                    .partnerName(partner.getPartnerName())
                    .partnerDetail(partner.getPartnerDetail())
                    .partnerBirth(partner.getPartnerBirth())
                    .gender(partner.getGender())
                    .selectionReason(partner.getSelectionReason())
                    .relationship(partner.getRelationship())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PartnerCreationReqDTO {
        private String stdId;
        private String partnerName;
        private String partnerDetail;
        private MultipartFile partnerPhoto;
        private String selectionReason;
        private String relationship;
        private String gender;
        private String partnerBirth;

        public Partner toPartner(Member member) {
            return Partner.builder()
                    .member(member)
                    .partnerName(this.partnerName)
                    .partnerBirth(replacePartnerBirth())
                    .gender(this.gender)
                    .selectionReason(this.selectionReason)
                    .partnerDetail(this.partnerDetail)
                    .relationship(this.relationship)
                    .partnerDivision(this.partnerDetail.equals("o") ? 0 : 1)
                    .build();
        }

        public String replacePartnerBirth() {
            return this.partnerBirth.replace('-','/');
        }

        public boolean isPartnerPhotoPresent() {
            return this.partnerPhoto != null;
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PartnerInfoChangeReqDTO {
        private String partnerId;
        private String partnerName;
        private String partnerDetail;
        private String selectionReason;
        private String relationship;
        private String gender;
        private String partnerBirth;
        private MultipartFile partnerPhoto;

        public boolean isPartnerNamePresent() {
            return this.partnerName != null;
        }

        public boolean isPartnerDetailPresent() {
            return this.partnerDetail != null;
        }

        public boolean isSelectionReasonPresent() {
            return this.selectionReason != null;
        }

        public boolean isRelationshipPresent() {
            return this.relationship != null;
        }

        public boolean isGenderPresent() {
            return this.gender != null;
        }

        public boolean isPartnerBirthPresent() {
            return this.partnerBirth != null;
        }

        public boolean isPartnerPhotoPresent() {
            return this.partnerPhoto != null;
        }

        public String replacePartnerBirth() {
            return this.partnerBirth.replace('-', '/');
        }
    }
}
