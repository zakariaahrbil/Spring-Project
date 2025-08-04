package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.AuthenticationRequest;
import com.example.SpringProjectApplication.Dtos.RegistrationRequest;
import com.example.SpringProjectApplication.Dtos.AuthenticationResponse;

public interface AuthenticationService {
     AuthenticationResponse register(RegistrationRequest request);
     AuthenticationResponse login(AuthenticationRequest request);
}