package app.demo.management.key;

import app.demo.management.key.aop.CountApiCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController {

    @CountApiCall
    @PostMapping("/api/v1/call/transfer")
    public ResponseEntity<String> handleApiCall() {
        log.info("Handling API call");
        return ResponseEntity.ok("Success");
    }
}
