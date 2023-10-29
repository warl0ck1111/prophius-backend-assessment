package com.example.backend.services;

import com.example.backend.exceptions.CommentException;
import com.example.backend.models.dtos.CommentResponse;
import com.example.backend.models.dtos.CommentResponseI;
import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.NotificationType;
import com.example.backend.repositories.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;


    @Transactional
    public void createComment(long userId, long postId, String content) {
        log.info("createComment/postId: {}", postId);
        log.info("createComment/contentLength: {}", content.length());
        Post post = postService.getPost(postId);
        User user = userService.getUser(userId);

        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .notificationType(NotificationType.COMMENT)
                .sender(user)
                .post(post)
                .user(post.getUser())
                .build();
        notificationService.createNotification(notificationRequest);
        createComment(user, post, content);
    }

    @Transactional
    public void updateComment(long commentId,Long userId, String content) {
        log.info("updateComment/commentId: {}", commentId);
        log.info("updateComment/contentLength: {}", content.length());
        Comment comment = getComment(commentId);

        if (userId.compareTo(comment.getUser().getId()) != 0){
            throw new CommentException("can not perform operation ");
        }
        comment.setContent(content);
        commentRepository.save(comment);
    }


    public Comment getComment(Long commentId) {
        return findCommentById(commentId);
    }


    public void deleteComment(Long commentId, long userId) {
        commentRepository.deleteByCommentIdAndUserId(commentId,userId );
    }
    public Page<CommentResponseI> getCommentForPost(Long postId, int page, int pageSize, String sortField, Sort.Direction sortDirection ) {
        Sort sort = Sort.by(sortDirection, sortField);
        return commentRepository.findAllByPost(postId, PageRequest.of(page, pageSize,sort ));
    }
    private Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentException(String.format("comment with Id: %s not found", commentId)));

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


    public Page<CommentResponseI> searchComments(String keyword, long postId, int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("searchComments/postId = {}", postId);
        log.info("searchComments/keyword = {}", keyword);
        log.info("searchComments/page = {}", page);
        log.info("searchComments/pageSize = {}", pageSize);
        log.info("searchComments/sortField = {}", sortField);
        log.info("searchComments/sortDirection = {}", sortDirection);
        Sort sort = Sort.by(sortDirection, sortField);
        if (Strings.isNotBlank(keyword)) {
            Post post = postService.getPost(postId);
            return commentRepository.searchCommentsByPostId(keyword, post, PageRequest.of(page, pageSize, sort));
        }else{
            throw new CommentException("please enter a search keyword");
        }
    }


}
