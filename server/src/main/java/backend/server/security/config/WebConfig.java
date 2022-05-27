package backend.server.security.config;

import backend.server.security.interceptor.AuthorizationValidationInterceptor;
import backend.server.security.interceptor.JwtTokenValidationInterceptor;
import backend.server.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtTokenValidationInterceptor(jwtTokenProvider))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/", "/signup", "/findpassword", "/signup/authNum",
                        "/auth", "/admin/*", "/returnId", "/test", "/member/**", "/error/**");

        registry.addInterceptor(new AuthorizationValidationInterceptor(jwtTokenProvider))
                .order(2)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/error/**");
    }


}
