package backend.server.service.user;

import backend.server.DTO.auth.LoginDTO;
import backend.server.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인
    @Transactional
    public LoginDTO login(LoginDTO.LoginReqDTO loginReqDTO) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReqDTO.getStdId(), loginReqDTO.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            String jwt = tokenProvider.createToken(authentication);

            return LoginDTO.LoginSuccessResDTO.builder()
                    .stdId(loginReqDTO.getStdId())
                    .token(jwt)
                    .build();

        } catch (Exception e) {

            return new LoginDTO.LoginFailResDTO();
        }
    }
}
