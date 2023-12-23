package com.ameda.kevin.jwt.service;



import com.ameda.kevin.jwt.config.JwtService;
import com.ameda.kevin.jwt.repository.UserRepository;
import com.ameda.kevin.jwt.request.AuthenticationRequest;
import com.ameda.kevin.jwt.request.RegisterRequest;
import com.ameda.kevin.jwt.response.AuthenticationResponse;
import com.ameda.kevin.jwt.user.Role;
import com.ameda.kevin.jwt.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user= User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user=userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("user with designated mail not found."));
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

}
