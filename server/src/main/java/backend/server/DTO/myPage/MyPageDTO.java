package backend.server.DTO.myPage;

import backend.server.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }
}
