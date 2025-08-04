package com.example.demoApplication.Services;


import com.example.demoApplication.Dtos.AuthenticationRequest;
import com.example.demoApplication.Dtos.AuthenticationResponse;
import com.example.demoApplication.Dtos.RegistrationRequest;
import com.example.demoApplication.Entities.Role;
import com.example.demoApplication.Entities.User;
import com.example.demoApplication.Exceptions.UserAlreadyExistsException;
import com.example.demoApplication.Repositories.UserRepository;
import com.example.demoApplication.security.PasswordEncoder;
import com.example.demoApplication.security.config.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        validateRegistrationRequest(request);
        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.bCryptPasswordEncoder().encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken)
                .email(user.getEmail()).username(user.getUsername()).role(user.getRole()).build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.getUsersByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken)
                .email(user.getEmail()).username(user.getUsername()).role(user.getRole()).build();
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
