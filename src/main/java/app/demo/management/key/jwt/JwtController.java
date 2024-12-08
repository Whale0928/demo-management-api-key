package app.demo.management.key.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtProvider provider;
    private final JwtTokenDecoder tokenDecoder;

    @GetMapping
    public ResponseEntity<?> getJwt() {
        final String token = provider.createToken("key-manager@email.com", TokenType.ACCESS);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/decode")
    public ResponseEntity<?> decodeJwt(@RequestParam String token) {
        return ResponseEntity.ok(tokenDecoder.decode(token));
    }
}
