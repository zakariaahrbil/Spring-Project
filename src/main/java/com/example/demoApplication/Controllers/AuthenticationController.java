package com.example.demoApplication.Controllers;


import com.example.demoApplication.Dtos.AuthenticationRequest;
import com.example.demoApplication.Dtos.RegistrationRequest;
import com.example.demoApplication.Dtos.AuthenticationResponse;
import com.example.demoApplication.Services.AuthenticationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest  user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest user) {
        return ResponseEntity.ok(authenticationService.login(user));
    }


}
