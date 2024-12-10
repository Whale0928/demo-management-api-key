package app.demo.management.key.security.authentication;

import app.demo.management.key.jwt.JwtTokenDecoder;
import app.demo.management.key.jwt.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String ROLE_PREFIX = "ROLE_";
    private final JwtTokenDecoder tokenDecoder;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        if (Objects.isNull(apiKey)) {
            throw new BadCredentialsException("api key required");
        }

        Map<String, Object> decode = tokenDecoder.decode(apiKey);
        TokenType tokenType = tokenDecoder.extractTokenType(apiKey);
        String role = ROLE_PREFIX + tokenType.getAuthority();
        log.info("AuthenticationService : decode: {}", decode);
        return new ApiKeyAuthentication(apiKey, AuthorityUtils.createAuthorityList(role), tokenType);
    }

}
