package com.example.SpringProjectApplication.Dtos;

import com.example.SpringProjectApplication.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String email;
    private String username;
    private Role role;
    private String token;
    private String refreshToken;


}
