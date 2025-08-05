package com.example.SpringProjectApplication.Controllers;


import com.example.SpringProjectApplication.Dtos.AuthenticationRequest;
import com.example.SpringProjectApplication.Dtos.AuthenticationResponse;
import com.example.SpringProjectApplication.Dtos.RegistrationRequest;
import com.example.SpringProjectApplication.Services.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;



    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest user) {
        return ResponseEntity.ok(authenticationService.login(user));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletRequest response) {

        return ResponseEntity.ok(authenticationService.refresh(request, response));

    }


    // For testing purposes only
//    @PostMapping(path = "/expired")
//    public ResponseEntity<String> expiredToken(@RequestBody AuthenticationRequest userRequest) {
//        System.out.println(userRequest);
//
//       User user = userRepository.findByEmail(userRequest.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userRequest.getEmail()));
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", user.getRole().name());
//        claims.put("username", user.getActualUsername());
//        claims.put("type", "refresh");
//       var expiredToken = jwtService.generateExpiredToken(user,claims);
//        return ResponseEntity.ok(expiredToken);
//    }


}
