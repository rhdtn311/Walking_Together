package backend.server.DTO.auth;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 로그인시 사용
public class LoginDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String stdId;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
