package app.demo.management.key;

import app.demo.management.key.jwt.JwtProvider;
import app.demo.management.key.jwt.JwtTokenDecoder;
import app.demo.management.key.jwt.TokenType;
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

    private final JwtProvider tokenProvider;
    private final JwtTokenDecoder tokenDecoder;

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
}
