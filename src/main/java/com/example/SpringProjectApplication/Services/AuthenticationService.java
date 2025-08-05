package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.AuthenticationRequest;
import com.example.SpringProjectApplication.Dtos.RegistrationRequest;
import com.example.SpringProjectApplication.Dtos.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
     AuthenticationResponse register(RegistrationRequest request);
     AuthenticationResponse login(AuthenticationRequest request);
     AuthenticationResponse refresh(HttpServletRequest request, HttpServletRequest response);

}