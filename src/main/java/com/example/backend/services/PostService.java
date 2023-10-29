package com.example.backend.services;

import com.example.backend.exceptions.PostException;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.dtos.CreatePostRequest;
import com.example.backend.models.dtos.CreatePostResponse;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.NotificationType;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest postRequest) {
        log.info("createPost/postRequest = {}", postRequest);
        User user = userService.getUser(postRequest.getUserId());
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .content(postRequest.getContent())
                .likesCount(0)
                .user(user)
                .build();
        Post newPost = postRepository.save(post);
        return CreatePostResponse.builder()
                .postId(newPost.getId())
                .content(post.getContent())
                .userId(user.getId())
                .username(user.getUsername())
                .creationDate(post.getCreationDate())
                .likesCount(post.getLikesCount())
                .build();

    }

    public Post getPost(Long postId) {
        log.info("getPost/postId = {}", postId);
        return findPostById(postId);
    }

    public void updatePost(Long postId,Long userId, String content) {
        log.info("updatePost/userId = {}", userId);
        log.info("updatePost/postId = {}", postId);
        log.info("updatePost/contentLength = {}", content.length());
        validatePostContent(content);

        Post post = findPostById(postId);
        //check to see if user who made first post is the same one trying to update it
        if (userId.compareTo(post.getUser().getId()) !=0){
            throw new PostException("can not perform this operation");
        }

        post.setContent(content);
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        log.info("deletePost/postId = {}", postId);

        Post post = findPostById(postId);

        // Delete associated comments
        List<Comment> comments = commentRepository.findAllByPost(post);
        commentRepository.deleteAll(comments);

        // Finally, delete the post
        postRepository.delete(post);

        //delete post
        postRepository.deleteById(postId);


    }

    @Transactional
    public void likePost(long postId, long userId) {
        log.info("likePost/postId = {}", postId);
        Post post = findPostById(postId);
        User user = userService.getUser(userId);
        post.setLikesCount(post.getLikesCount() + 1);

        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .notificationType(NotificationType.LIKE)
                .sender(user)
                .post(post)
                .user(post.getUser())
                .build();
        notificationService.createNotification(notificationRequest);
    }


    public Page<Post> searchPosts(String keyword, int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("searchPosts/keyword = {}", keyword);
        log.info("searchPosts/page = {}", page);
        log.info("searchPosts/pageSize = {}", pageSize);
        log.info("searchPosts/sortField = {}", sortField);
        log.info("searchPosts/sortDirection = {}", sortDirection);
        Sort sort = Sort.by(sortDirection, sortField);
        if (Strings.isNotBlank(keyword)) {
            return postRepository.searchPosts(keyword, PageRequest.of(page, pageSize, sort));
        }else{
            throw new PostException("provide a search keyword");
        }
    }

    public Page<Post> getAllPosts(int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("getAllPosts/page = {}", page);
        log.info("getAllPosts/pageSize = {}", pageSize);
        log.info("getAllPosts/sortField = {}", sortField);
        log.info("getAllPosts/sortDirection = {}", sortDirection);
        Sort sort = Sort.by(sortDirection, sortField);
        return postRepository.findAll(PageRequest.of(page, pageSize, sort));
    }


    private Post findPostById(long postId) {
        log.info("findPostById/postId = {}", postId);
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("post with id:%s not found", postId)));
    }

    private void validatePostContent(String content) {
        if (Strings.isBlank(content)) throw new PostException("invalid post");
    }


}
