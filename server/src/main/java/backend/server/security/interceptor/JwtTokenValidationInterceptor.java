package backend.server.security.interceptor;

import backend.server.security.exception.TokenInvalidationException;
import backend.server.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenValidationInterceptor implements HandlerInterceptor {

    private final String authorityHeader = JwtTokenProvider.AUTHORITY_HEADER;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("사용자 인증 시작");
        String header = request.getHeader(authorityHeader);
        log.info("header : {}", header);

        if (header != null) {
            String token = jwtTokenProvider.getTokenByHeader(header);
            if (jwtTokenProvider.isTokenValid(token)) {
                return true;
            } else {
                throw new TokenInvalidationException();
            }
        }

        return false;
    }
}
