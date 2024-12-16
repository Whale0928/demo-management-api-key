package app.demo.management.key.client;

import app.demo.management.key.jwt.TokenType;

import java.util.List;

public record ClientRegisterRequest(
        String name,
        String email,
        String issuerInfo,
        TokenType tokenType,
        List<String> allowedIps
) {
}
