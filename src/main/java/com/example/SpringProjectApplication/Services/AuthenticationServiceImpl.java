package com.example.SpringProjectApplication.Services;


import com.example.SpringProjectApplication.Dtos.AuthenticationRequest;
import com.example.SpringProjectApplication.Dtos.AuthenticationResponse;
import com.example.SpringProjectApplication.Dtos.RegistrationRequest;
import com.example.SpringProjectApplication.Entities.Role;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Exceptions.UserAlreadyExistsException;
import com.example.SpringProjectApplication.Repositories.UserRepository;
import com.example.SpringProjectApplication.security.config.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        validateRegistrationRequest(request);

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(true)
                .build();

        userRepository.save(user);

        // Include role in token claims
        var jwtToken = jwtService.generateToken(
                Map.of("role", user.getRole().name()),
                user
        );

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        System.out.println("User not found in database");
                        return new UsernameNotFoundException("User not found");
                    });

            System.out.println("User found: " + user.getEmail());
            System.out.println("Account enabled: " + user.isEnabled());
            System.out.println("Password matches: " +
                    passwordEncoder.matches(request.getPassword(), user.getPassword()));


            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );


             user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("username", user.getActualUsername());


            var jwtToken = jwtService.generateToken(
                    claims,
                    user
            );

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .username(user.getActualUsername())
                    .role(user.getRole())
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password",e.getCause());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private void validateRegistrationRequest(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email Already Exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username Already Exists");
        }
        if (request.getEmail().length() < 5 || !request.getEmail().contains("@")) {
            throw new BadCredentialsException("Invalid email format");
        }
        if (request.getUsername().length() < 3) {
            throw new BadCredentialsException("Username must be at least 3 characters long");
        }
        if (request.getPassword().length() < 8) {
            throw new BadCredentialsException("Password must be at least 8 characters long");
        }
    }
}