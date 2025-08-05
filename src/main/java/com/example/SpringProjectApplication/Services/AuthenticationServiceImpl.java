package com.example.SpringProjectApplication.Services;


import com.example.SpringProjectApplication.Dtos.AuthenticationRequest;
import com.example.SpringProjectApplication.Dtos.AuthenticationResponse;
import com.example.SpringProjectApplication.Dtos.RegistrationRequest;
import com.example.SpringProjectApplication.Entities.Role;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Exceptions.UserAlreadyExistsException;
import com.example.SpringProjectApplication.Repositories.UserRepository;
import com.example.SpringProjectApplication.security.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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
        var tokens = getTokens(user);

        return AuthenticationResponse.builder()
                .token(tokens[0])
                .refreshToken(tokens[1])
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

             User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var tokens = getTokens(user);

            return AuthenticationResponse.builder()
                    .token(tokens[0])
                    .refreshToken(tokens[1])
                    .email(user.getEmail())
                    .username(user.getActualUsername())
                    .role(user.getRole())
                    .build();

        }catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    @Override
    public AuthenticationResponse refresh(HttpServletRequest request, HttpServletRequest response) {
        String header = request.getHeader("X-Refresh-Token");

        if(header == null || !header.startsWith("Bearer " )) {
            throw new BadCredentialsException("Invalid Authorization header");
        }

        String token = header.substring(7);
        if(!jwtService.isRefreshToken(token)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(token);
        if(email == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if(!jwtService.isTokenValid(token,user)){
            throw new BadCredentialsException("Invalid token");
        }

        var tokens = getTokens(user);

        return AuthenticationResponse.builder()
                .token(tokens[0])
                .refreshToken(tokens[1])
                .email(user.getEmail())
                .username(user.getActualUsername())
                .role(user.getRole())
                .build();


    }


    private String[] getTokens(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("username", user.getActualUsername());

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("role", user.getRole().name());
        refreshClaims.put("username", user.getActualUsername());
        refreshClaims.put("type", "refresh");


        var jwtToken = jwtService.generateToken(
                claims,
                user
        );

        var jwtRefreshToken = jwtService.generateRefreshToken(
                refreshClaims,
                user
        );
        return new String[]{jwtToken, jwtRefreshToken};
    }

    private void validateRegistrationRequest(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already in use");
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