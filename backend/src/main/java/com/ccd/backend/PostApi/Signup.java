import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/register")
    public ResponseEntity<String> registerUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password) {


        if (username == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameters");
        }

        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }
}
