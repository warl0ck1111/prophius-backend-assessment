package com.example.backend.controllers;


import com.example.backend.models.dtos.CommentRequest;
import com.example.backend.models.entities.Comment;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.services.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/")
    public void createComment(@RequestBody CommentRequest request) {
        commentService.createComment(request.getUserId(), request.getPostId(), request.getContent());
    }


    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable long commentId, @RequestBody String content) {
        commentService.updateComment(commentId, content);
    }


    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<List<Comment>> getCommentForPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentForPost(postId);
        return ResponseEntity.ok(comments);
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.info("handleException/");
        log.error("handleException/exception=" + e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
