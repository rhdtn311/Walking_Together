package backend.server.security.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class UserToMemberDTO {

    String id;
    String password;
    String authorities;


    @Builder
    public UserToMemberDTO(String id, String password, String authorities) {
        this.id = id;
        this.password = password;
        this.authorities = authorities;
    }
}

