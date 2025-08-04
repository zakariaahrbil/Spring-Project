package com.example.SpringProjectApplication.Dtos;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String email;
    private String password;
    private String username;

}
