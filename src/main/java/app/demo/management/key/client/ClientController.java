package app.demo.management.key.client;

import app.demo.management.key.jwt.JwtProvider;
import app.demo.management.key.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientRepository clientRepository;
    private final JwtProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(
            @RequestBody ClientRegisterRequest request
    ) {
        final String token = tokenProvider.createToken(request.issuerInfo(), request.email(), TokenType.TEMPORARY);
        Client client = clientRepository.save(
                Client.builder()
                        .name(request.name())
                        .email(request.email())
                        .apiKey(token)
                        .issuerInfo(request.issuerInfo())
                        .permissions(
                                request.tokenType() == null ?
                                        PermissionsType.defaultPermissions() : PermissionsType.permissionsByTokenType(request.tokenType())
                        )
                        .allowedIps(request.allowedIps().toArray(new String[0]))
                        .build()
        );
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getClient(@PathVariable String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return ResponseEntity.ok(client);
    }
}
