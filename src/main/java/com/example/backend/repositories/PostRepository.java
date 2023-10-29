package com.example.backend.repositories;

import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Post> searchPosts(String keyword, Pageable pageable);

    List<Post> findAllByUser(User user);
}
