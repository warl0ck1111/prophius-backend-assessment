package com.example.backend.controllers;


import com.example.backend.exceptions.PostException;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.models.dtos.CreatePostRequest;
import com.example.backend.models.dtos.CreatePostResponse;
import com.example.backend.models.dtos.UpdatePostRequest;
import com.example.backend.models.entities.Post;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.response.ApiFailedResponse;
import com.example.backend.services.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

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
    @PostMapping("/create")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody @Valid CreatePostRequest postRequest) {
        CreatePostResponse post = postService.createPost(postRequest);
        return ResponseEntity.ok(post);
    }

    // Update a post
    @PutMapping("/{postId}/update")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        postService.updatePost(postId, updatePostRequest.getContent());
        return ResponseEntity.ok().build();
    }

    // Delete a post
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Like a post
    @PutMapping("/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    // Post search endpoint
    @GetMapping("/search/posts")
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


    /**
     * Exceptions Handlers
     */


    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiFailedResponse> handlePostException(PostException e) {
        log.info("UserException/");
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
        log.error("handleException/e="+e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
