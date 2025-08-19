package com.example.SpringProjectApplication.Dtos;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfileDto
{
    @Null
    private Long id;
    @Null
    private String username;
    @Null
    private String email;
    private String profilePictureUrl;
    private LocalDate birthday;
    private String phoneNumber;
    private String address;
    private String fullName;
    private Gender gender;

}
