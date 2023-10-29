package com.example.backend.services;

import com.example.backend.exceptions.CommentException;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.repositories.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;


    @Transactional
    public void createComment(long userId, long postId, String content) {
        log.info("createComment/postId: {}", postId);
        log.info("createComment/contentLength: {}", content.length());
        Post post = postService.getPost(postId);
        User user = userService.getUser(userId);
        createComment(user, post, content);
    }

    @Transactional
    public void updateComment(long commentId, String content) {
        log.info("updateComment/commentId: {}", commentId);
        log.info("updateComment/contentLength: {}", content.length());
        Comment comment = getComment(commentId);
        comment.setContent(content);
        commentRepository.save(comment);
    }


    public Comment getComment(Long commentId) {
        return findCommentById(commentId);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    public List<Comment> getCommentForPost(Long postId) {
        Post post = postService.getPost(postId);
        return commentRepository.findAllByPost(post);

    }
    private Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentException("comment not found"));

    }

    private void validateComment(String content) {
        if (Strings.isBlank(content)) throw new CommentException("invalid comment");
    }

    private void createComment(User user, Post post, String comment) {
        log.info("createComment/post/contentLength = {}", post.getContent().length());
        validateComment(comment);
        Comment newComment = Comment.builder()
                .post(post)
                .user(user)
                .content(comment)
                .creationDate(LocalDateTime.now())
                .build();
        commentRepository.save(newComment);

    }


    public List<Comment> findByUser(User user) {
        return commentRepository.findAllByUser(user);
    }

    public void saveComment(Comment comment){
        commentRepository.save(comment);
    }
}
