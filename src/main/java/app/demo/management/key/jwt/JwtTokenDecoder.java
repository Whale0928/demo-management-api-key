package app.demo.management.key.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenDecoder {
    private final SecretKey key;

    public JwtTokenDecoder(JwtConfig jwtConfig) {
        byte[] keyBytes = jwtConfig.getSecret().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * jwt 토큰의 모든 정보를 반환한다. (테스트용)
     */
    public Map<String, Object> decode(String token) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("claimsJws", claimsJws);
        return tokenInfo;
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parse(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰");
        } catch (UnsupportedJwtException e) {
            log.error("지원 되지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 비어 있음");
        }
        return false;
    }

    /**
     * 토큰에서 subject(토큰 발행 주체)를 추출한다.
     */
    public String extractReferer(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 토큰에서 email을 추출한다.
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * 토큰에서 TokenType을 추출한다.
     */
    public TokenType extractTokenType(String token) {
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return TokenType.valueOf(type);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }
}
