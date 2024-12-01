package app.demo.management.key.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/home")
    public String homeEndpoint() {
        return "Baeldung !";
    }
}
