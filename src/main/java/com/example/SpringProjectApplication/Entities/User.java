package com.example.SpringProjectApplication.Entities;


import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(columnDefinition = "tinyint(1)")
    private boolean verified ;
    private String profilePictureUrl;
    private LocalDate birthday;
    private String phoneNumber;
    private String address;
    private String fullName;
    private Gender gender;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Return email as the username for authentication
    @Override
    public String getUsername() {
        return email;
    }

    // Helper method to get actual username
    public String getActualUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }
}
