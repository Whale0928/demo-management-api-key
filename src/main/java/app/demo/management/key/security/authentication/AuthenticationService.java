package app.demo.management.key.security.authentication;

import app.demo.management.key.client.Client;
import app.demo.management.key.client.ClientRepository;
import app.demo.management.key.jwt.JwtTokenDecoder;
import app.demo.management.key.jwt.TokenType;
import app.demo.management.key.security.authentication.ApiKeyAuthentication.Principal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String ROLE_PREFIX = "ROLE_";
    private final JwtTokenDecoder tokenDecoder;
    private final ClientRepository clientRepository;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        if (Objects.isNull(apiKey)) {
            throw new BadCredentialsException("api key required");
        }
        TokenType tokenType = tokenDecoder.extractTokenType(apiKey);
        String email = tokenDecoder.extractEmail(apiKey);
        String role = ROLE_PREFIX + tokenType.getAuthority();

        Client client = clientRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("not fount client email:" + email));

        Principal principal = Principal.builder()
                .email(client.getEmail())
                .issuerInfo(client.getIssuerInfo())
                .permissions(List.of(client.getPermissions()))
                .allowedIps(List.of(client.getAllowedIps()))
                .issuedAt(client.getIssuedAt())
                .build();
        return new ApiKeyAuthentication(apiKey, AuthorityUtils.createAuthorityList(role), principal);
    }
}
