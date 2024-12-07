package app.demo.management.key.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final JwtConfig jwtConfig;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        byte[] keyBytes = jwtConfig.getSecret().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, TokenType tokenType) {
        Long expirationTime = jwtConfig.getExpirationTime();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", tokenType.getValue());
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(tokenType.getSubject())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationTime))
                .signWith(key)
                .compact();
    }
}
