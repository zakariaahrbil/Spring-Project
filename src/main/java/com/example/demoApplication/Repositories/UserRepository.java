package com.example.demoApplication.Repositories;

import com.example.demoApplication.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUsersByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
