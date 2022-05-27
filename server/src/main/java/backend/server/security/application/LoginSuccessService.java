package backend.server.security.application;

import backend.server.DTO.auth.LoginDTO;
import backend.server.security.dto.UserToMemberDTO;
import backend.server.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginSuccessService extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_VALUE = "Bearer";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException, IOException {
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtTokenProvider.getJwtToken(createMemberDTO(user, authentication));

        response.addHeader(AUTH_HEADER, AUTH_VALUE + " " + jwtToken);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginSuccessResDTO(user, jwtToken)));
    }
    private LoginDTO.LoginSuccessResDTO loginSuccessResDTO(User user, String jwtToken) {
        return LoginDTO.LoginSuccessResDTO.builder()
                .stdId(user.getUsername())
                .token(jwtToken)
                .build();
    }

    public UserToMemberDTO createMemberDTO(User user, Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserToMemberDTO userToMemberDTO = new UserToMemberDTO();
        userToMemberDTO.setId(user.getUsername());
        userToMemberDTO.setPassword(user.getPassword());
        userToMemberDTO.setAuthorities(authorities);

        return userToMemberDTO;
    }
}
