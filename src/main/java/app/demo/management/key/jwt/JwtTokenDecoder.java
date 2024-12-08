package app.demo.management.key.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenDecoder {

    private final SecretKey key;

    public JwtTokenDecoder(JwtConfig jwtConfig) {
        byte[] keyBytes = jwtConfig.getSecret().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parse(token);
            return !isTokenExpired(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // 로그 처리
        } catch (ExpiredJwtException e) {
            // 로그 처리
        } catch (UnsupportedJwtException e) {
            // 로그 처리
        } catch (IllegalArgumentException e) {
            // 로그 처리
        }
        return false;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public TokenType extractTokenType(String token) {
        short tokenTypeValue =
                extractClaim(token, claims -> Short.parseShort(claims.get("type", String.class)));
        return TokenType.fromValue(tokenTypeValue);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Object decode(String token) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        Claims payload = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("claimsJws", claimsJws);
        tokenInfo.put("payload", payload);
        return tokenInfo;
    }
}
