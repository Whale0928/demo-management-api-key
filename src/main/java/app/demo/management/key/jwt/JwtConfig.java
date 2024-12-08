package app.demo.management.key.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
}
