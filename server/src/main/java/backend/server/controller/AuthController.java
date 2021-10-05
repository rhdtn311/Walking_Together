package backend.server.controller;

import backend.server.DTO.auth.CrossPageValidationResDTO;
import backend.server.DTO.auth.LoginDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.security.jwt.JwtFilter;
import backend.server.security.jwt.TokenProvider;
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity authorize(@Valid @RequestBody LoginDTO.LoginReqDTO loginReqDTO) {

//      LoginDTO 의 username, password 를 파라미터로 받아서 UsernamePasswordAuthenticationToken 을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReqDTO.getStdId(), loginReqDTO.getPassword());

        /* authenticationToken을 이용하여 Authentication 객체를 생성하기 위해 authenticate() 메소드가 실행될 때
         CustomUserDetailsService에서 loadUserByUsername 메소드가 실행된다.
         그 결과값을 가지고 authentication 객체를 생성하고 그 객체를 SecurityContextHolder에 저장한다.

         사용자의 이메일, 패스워드를 사용해서 UsernamePasswordAuthenticationToken 인증 객체를 만든다.
         그리고, 해당 객체에 저장된 패스워드가 실제 DB 에 저장된 비밀번호가 맞는지 검증이 필요한데,
         해당 과정을 수행하는 로직이 authenicate 메서드이다.*/
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 로그인에 실패했다면

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 앞서 만든 authentication(인증정보)를 기준으로  jwt 토큰을 생성하게 된다.
            String jwt = tokenProvider.createToken(authentication);

            // jwt 토큰을 Response Header에 넣어주고 TokenDTO를 이용하여 Response Body에도 넣어서 리턴한다.
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity(LoginDTO.LoginSuccessResDTO.builder()
                    .stdId(loginReqDTO.getStdId())
                    .token(jwt).build()
                    , httpHeaders, HttpStatus.OK);

        } catch(Exception e) {
            return new ResponseEntity(new LoginDTO.LoginFailResDTO() ,HttpStatus.OK);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<CrossPageValidationResDTO> isAuth(@RequestBody TokenDTO tokenDTO) {
        boolean isAuth = tokenProvider.validateToken(tokenDTO.getToken());

        return isAuth ? ResponseEntity.ok(CrossPageValidationResDTO.builder().isAuth(true).role(tokenProvider.getAuthentication(tokenDTO.getToken()).getAuthorities()).stdId(tokenProvider.getAuthentication(tokenDTO.getToken()).getName()).build())
                : new ResponseEntity<>(CrossPageValidationResDTO.builder().isAuth(false).build(), HttpStatus.BAD_REQUEST);
    }
}
