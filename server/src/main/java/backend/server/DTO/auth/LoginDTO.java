package backend.server.DTO.auth;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.extern.java.Log;

import javax.validation.constraints.Size;

public class LoginDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginReqDTO {
        @NotNull
        @Size(min = 3, max = 50)
        private String stdId;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginSuccessResDTO {
        private String stdId;
        private String token;

        private final boolean success = true;

        @Builder
        public LoginSuccessResDTO(String stdId, String token) {
            this.stdId = stdId;
            this.token = token;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LoginFailResDTO {
        private final boolean success = false;
    }
}
