package backend.server.security.jwt;

import backend.server.entity.MemberRole;
import backend.server.security.dto.UserToMemberDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String AUTHORITY_HEADER = "Authorization";

    private String secret;
    private Long tokenDuration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.duration}") Long tokenDuration) {
        this.secret = secret;
        this.tokenDuration = tokenDuration;
    }

    public String getStdIdFromToken(String token) {
        return this.getClaimsFromToken(token).getBody().get("id",String.class);
    }

    public String getAuthorizationByToken(String token) {
        return this.getClaimsFromToken(token).getBody().get("auth", String.class);
    }

    public String getJwtToken(UserToMemberDTO member) {

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaim(member))
                .setExpiration(getExpiration())
                .signWith(SignatureAlgorithm.HS256, createKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaimsFromToken(token).getBody();
            log.info("validation success = {}", claims.getSubject());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("token expired : {}", e.getMessage());
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("incorrect token = {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("unsupported token = {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("token error = {}", e.getMessage());
            return false;
        }
    }

    public String getTokenByHeader(String header) {
        return header.split(" ")[1];
    }

    private Jws<Claims> getClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
    }

    private Date getExpiration() {

        // 12시간
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, 12);
        return cal.getTime();
    }

    private Key createKey() {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return header;
    }

    private Map<String, Object> createClaim(UserToMemberDTO member) {

        Map<String, Object> claim = new HashMap<>();

        claim.put("sub", member.getId() + " JWT token");
        claim.put("www.walking2gether.com", true);
        claim.put("id", member.getId());
        claim.put("auth", member.getAuthorities());

        return claim;
    }
}
