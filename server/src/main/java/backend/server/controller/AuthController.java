package backend.server.controller;

import backend.server.DTO.auth.CrossPageValidationResDTO;
import backend.server.DTO.auth.LoginDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.security.jwt.JwtFilter;
import backend.server.security.jwt.TokenProvider;
import backend.server.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthService authService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginDTO.LoginReqDTO loginReqDTO) {

        LoginDTO loginResult = authService.login(loginReqDTO);

        if (loginResult instanceof LoginDTO.LoginSuccessResDTO) {

            String jwt = ((LoginDTO.LoginSuccessResDTO) loginResult).getToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(LoginDTO.LoginSuccessResDTO.builder()
                    .stdId(loginReqDTO.getStdId())
                    .token(jwt).build()
                    , httpHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>(new LoginDTO.LoginFailResDTO() ,HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/auth")
    public ResponseEntity<CrossPageValidationResDTO> isAuth(@RequestBody TokenDTO tokenDTO) {
        boolean isAuth = tokenProvider.validateToken(tokenDTO.getToken());

        return isAuth ? ResponseEntity.ok(CrossPageValidationResDTO.builder().isAuth(true).role(tokenProvider.getAuthentication(tokenDTO.getToken()).getAuthorities()).stdId(tokenProvider.getAuthentication(tokenDTO.getToken()).getName()).build())
                : new ResponseEntity<>(CrossPageValidationResDTO.builder().isAuth(false).build(), HttpStatus.BAD_REQUEST);
    }
}
