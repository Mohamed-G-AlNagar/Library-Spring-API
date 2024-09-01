package com.SecurityDemo.jwttest.auth;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication") // for issue swagger group called Authentication and group all below endpoints under it
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
//        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    // token as endpoint variable
//    @GetMapping("/activate-account/{token}")
//    public void confirm(@PathVariable String token
//    ) throws MessagingException {
//        service.activateAccount(token);
//    }

    // token as params variable
    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("-----------Log test --------------------");
        return ResponseEntity.ok("Hello from test endpoint!");
    }

}
