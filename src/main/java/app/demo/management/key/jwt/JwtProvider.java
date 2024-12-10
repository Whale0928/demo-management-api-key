package app.demo.management.key.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
    private final SecretKey key;

    public JwtProvider(JwtConfig jwtConfig) {
        byte[] keyBytes = jwtConfig.getSecret().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String referer, String email, TokenType tokenType) {
        Map<String, Object> claims = Map.of(
                "email", email,
                "type", tokenType,
                "level", tokenType.getLevel(),
                "authority", tokenType.getAuthority(),
                "description", tokenType.getDescription()
        );

        return Jwts.builder()
                .claims(claims)
                .subject(referer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(key)
                .compact();
    }
}
