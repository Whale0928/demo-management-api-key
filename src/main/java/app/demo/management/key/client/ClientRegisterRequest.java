package app.demo.management.key.client;

import java.util.List;

public record ClientRegisterRequest(
        String name,
        String email,
        String issuerInfo,
        List<String> allowedIps
) {
}
