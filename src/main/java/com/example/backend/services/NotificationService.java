package com.example.backend.services;


import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.entities.Notification;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostService postService;
    private final UserService userService;


    public Notification createNotification(CreateNotificationRequest notificationRequest) {
        User recipientUser = userService.getUser(notificationRequest.getUserId());
        User notificationSender = userService.getUser(notificationRequest.getSenderId());
        Post post = postService.getPost(notificationRequest.getPostId());

        Notification notification = Notification.builder()
                .sender(notificationSender)
                .user(recipientUser)
                .post(post)
                .notificationType(notificationRequest.getNotificationType())
                .createdAt(LocalDateTime.now())
                .isRead(false).build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void markNotificationAsRead(long notificationId) {
        Notification notification = findNotificationById(notificationId);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    //todo add get only unread notifications impl...


    private Notification findNotificationById(long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new NoSuchElementException("notification not found"));
    }
}
