package com.example.SpringProjectApplication.Repositories;

import com.example.SpringProjectApplication.Entities.Role;
import com.example.SpringProjectApplication.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findAllByRole(Role role);
}
