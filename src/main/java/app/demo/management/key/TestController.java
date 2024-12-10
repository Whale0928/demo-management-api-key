package app.demo.management.key;

import app.demo.management.key.jwt.TokenType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/full")
    public ResponseEntity<String> full() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        return ResponseEntity.ok(TokenType.FULL.toString());
    }

    @GetMapping("/limited")
    public ResponseEntity<String> limited() {
        return ResponseEntity.ok(TokenType.LIMITED.toString());
    }

    @GetMapping("/temporary")
    public ResponseEntity<String> temporary() {
        return ResponseEntity.ok(TokenType.TEMPORARY.toString());
    }

}
