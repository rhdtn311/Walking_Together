package backend.server.DTO.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class CrossPageValidationResDTO {
    private boolean isIsAuth;
    private Collection<? extends GrantedAuthority> role;
    private String stdId;

    @Builder
    public CrossPageValidationResDTO(boolean isAuth, Collection<? extends GrantedAuthority> role, String stdId) {
        this.isIsAuth = isAuth;
        this.role = role;
        this.stdId = stdId;
    }

}
