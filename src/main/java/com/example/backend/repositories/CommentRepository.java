package com.example.backend.repositories;

import com.example.backend.models.dtos.CommentResponse;
import com.example.backend.models.dtos.CommentResponseI;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c.id as commentId, c.content as content,c.user.id as userId, c.creationDate as creationDate FROM Comment c WHERE c.post.id = :postId")
    Page<CommentResponseI> findAllByPost(@Param("postId") Long postId, Pageable pageable);


    List<Comment> findAllByPost(Post post);
    void deleteAllByPost(Post post);

    List<Comment> findAllByUser(User user);

    @Modifying
    @Query(value = "delete from comments where id = :commentId AND user = :userId ", nativeQuery = true)
    void deleteByCommentIdAndUserId(@Param("commentId")Long commentId, @Param("userId") long userId);


    @Query(value = "SELECT c.id as commentId, c.content as content, c.user.id as userId  FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')) AND c.post = :postId")
    Page<CommentResponseI> searchCommentsByPostId(@Param("keyword") String keyword, @Param("postId") Post postId,
                                                  Pageable pageable);
}
