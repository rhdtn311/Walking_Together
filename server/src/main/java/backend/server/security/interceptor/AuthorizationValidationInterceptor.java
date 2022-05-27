package backend.server.security.interceptor;

import backend.server.DTO.response.ResponseDTO;
import backend.server.exception.ErrorCode;
import backend.server.exception.ErrorResponse;
import backend.server.security.jwt.JwtTokenProvider;
import com.google.gson.Gson;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationValidationInterceptor implements HandlerInterceptor {

    private final String authorityHeader = JwtTokenProvider.AUTHORITY_HEADER;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("사용자 권한 검증 시작");
        String header = request.getHeader(authorityHeader);

        if (header != null) {
            String token = jwtTokenProvider.getTokenByHeader(header);
            String auth = jwtTokenProvider.getAuthorizationByToken(token);

            if (auth.equals("ROLE_ADMIN")) {
                return true;
            }
        }

        authorizationFailResponse(response);

        log.info("사용자 권한 검증 실패");
        return false;
    }

    private void authorizationFailResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.AUTHORIZATION_FAIL_EXCEPTION);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(errorResponse));

    }
}
