package com.example.backend.controllers;


import com.example.backend.exceptions.CommentException;
import com.example.backend.exceptions.PostException;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.models.dtos.*;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.response.ApiFailedResponse;
import com.example.backend.services.CommentService;
import com.example.backend.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    // Get posts with pagination and sorting
    @GetMapping("/list")
    public ResponseEntity<Page<Post>> getPostsByPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        Page<Post> posts = postService.getAllPosts(page, pageSize, sortField, sortDirection);
        return ResponseEntity.ok(posts);
    }

    // Create a new post
    @PostMapping("/")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest postRequest) {
        CreatePostResponse post = postService.createPost(postRequest);
        return ResponseEntity.ok(post);
    }

    // Update a post
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        postService.updatePost(postId, updatePostRequest.getUserId(), updatePostRequest.getContent());
        return ResponseEntity.ok().build();
    }

    // Delete a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Like a post
    @PutMapping("/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    // Post search endpoint
    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection

    ) {
        Page<Post> posts = postService.searchPosts(keyword, page, pageSize, sortField, sortDirection);
        return ResponseEntity.ok(posts);
    }


    //create post's comment
    @PostMapping("/{postId}/comments")
    public void createComment(@RequestBody CommentRequest request) {
        commentService.createComment(request.getUserId(), request.getPostId(), request.getContent());
    }


    //edit post's comment
    @PutMapping("/{postId}/comments/{commentId}")
    public void updateComment(@PathVariable long commentId, @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(commentId, request.getUserId(), request.getContent());
    }


    //get all comments for post
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    //get
    @GetMapping("/{postId}/comments/list")
    public ResponseEntity<Page<CommentResponseI>> getCommentForPost(@PathVariable Long postId,
                                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                                   @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
                                                                   @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        log.info("getCommentForPost/postId = {}", postId);
        log.info("getCommentForPost/page = {}", page);
        log.info("getCommentForPost/pageSize = {}", pageSize);
        log.info("getCommentForPost/sortField = {}", sortField);
        log.info("getCommentForPost/sortDirection = {}", sortDirection);
        Page<CommentResponseI> comments = commentService.getCommentForPost(postId, page,pageSize,sortField,sortDirection);
        return ResponseEntity.ok(comments);
    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @RequestParam long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // Post search endpoint
    @GetMapping("/{postId}/comments/search")
    public ResponseEntity<Page<CommentResponseI>> searchComments(
            @PathVariable Long postId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection

    ) {
        Page<CommentResponseI> posts = commentService.searchComments(keyword, postId, page, pageSize, sortField, sortDirection);
        return ResponseEntity.ok(posts);
    }


    /**
     * Exceptions Handlers
     */


    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiFailedResponse> handlePostException(PostException e) {
        log.info("handlePostException/e=" + e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiFailedResponse> handlePostNotFoundException(PostNotFoundException e) {
        log.error("PostNotFoundException/e=" + e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.info("handleException/");
        log.error("handleException/e=" + e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ApiErrorResponse> handleCommentException(CommentException e) {
        log.info("handleCommentException/");
        log.error("handleCommentException/exception=" + e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
