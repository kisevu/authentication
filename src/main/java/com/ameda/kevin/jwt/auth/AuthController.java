package com.ameda.kevin.jwt.auth;

import com.ameda.kevin.jwt.request.AuthenticationRequest;
import com.ameda.kevin.jwt.request.RegisterRequest;
import com.ameda.kevin.jwt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
