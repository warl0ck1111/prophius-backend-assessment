package com.example.backend.repositories;



import com.example.backend.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<User> searchUsers(String keyword, PageRequest pageRequest);

    
    Optional<User> findByEmail(String email);

}
