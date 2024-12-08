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
    public ResponseEntity<?> getJwt() {
        final String referer = "외부의 클라이언트";
        final String mail = "key-manager@email.com";
        final String token = tokenProvider.createToken(referer, mail, TokenType.FULL);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/full-decode")
    public ResponseEntity<?> decodeJwt(@RequestParam String token) {
        return ResponseEntity.ok(tokenDecoder.decode(token));
    }
}
