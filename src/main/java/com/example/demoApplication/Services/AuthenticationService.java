package com.example.demoApplication.Services;

import com.example.demoApplication.Dtos.AuthenticationRequest;
import com.example.demoApplication.Dtos.RegistrationRequest;
import com.example.demoApplication.Dtos.AuthenticationResponse;

public interface AuthenticationService {
     AuthenticationResponse register(RegistrationRequest request);
     AuthenticationResponse login(AuthenticationRequest request);
}