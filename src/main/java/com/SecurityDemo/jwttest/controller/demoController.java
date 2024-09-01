package com.SecurityDemo.jwttest.controller;

import com.SecurityDemo.jwttest.auth.AuthenticationResponse;
import com.SecurityDemo.jwttest.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo")
public class demoController {

    @GetMapping
    public ResponseEntity<String> hello(
    ) {
        return ResponseEntity.ok("Hello World123");
    }
}
