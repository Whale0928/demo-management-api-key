package app.demo.management.key.security;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    private final String apiKey;

    public ApiKeyAuthentication(
            String apiKey,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        log.info("getCredentials() called");
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        log.info("getPrincipal() called");
        return apiKey;
    }
}
