package com.example.backend.repositories;

import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    void deleteAllByPost(Post post);

    List<Comment> findAllByUser(User user);
}
