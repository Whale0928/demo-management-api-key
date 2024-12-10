package app.demo.management.key;

import app.demo.management.key.client.Client;
import app.demo.management.key.client.ClientRepository;
import app.demo.management.key.client.PermissionsType;
import app.demo.management.key.jwt.JwtProvider;
import app.demo.management.key.jwt.JwtTokenDecoder;
import app.demo.management.key.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtProvider tokenProvider;
    private final JwtTokenDecoder tokenDecoder;
    private final ClientRepository clientRepository;

    @GetMapping("/create")
    public ResponseEntity<?> getJwt(
            @RequestParam String referer,
            @RequestParam String email,
            @RequestParam TokenType tokenType
    ) {
        final String token = tokenProvider.createToken(referer, email, tokenType);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/full-decode")
    public ResponseEntity<?> decodeJwt(@RequestParam String token) {
        return ResponseEntity.ok(tokenDecoder.decode(token));
    }

    @GetMapping("/create-client")
    public ResponseEntity<?> createClient(
    ) {
        String key = String.valueOf(UUID.randomUUID());
        Client client = clientRepository.save(
                Client.builder()
                        .name(key)
                        .email(key + "@test.com")
                        .apiKey("key : " + key)
                        .issuerInfo("issuer : " + key)
                        .permissions(new PermissionsType[]{PermissionsType.READ, PermissionsType.WRITE})
                        .allowedIps(new String[]{"127.0.0.1", "*"})
                        .build()
        );
        return ResponseEntity.ok(client);
    }

    @GetMapping("/client")
    public ResponseEntity<?> getClient(
    ) {
        return ResponseEntity.ok(clientRepository.findAll().getFirst());
    }
}
