package com.example.backend.services;


import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.entities.Notification;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;



    public Notification createNotification(CreateNotificationRequest notificationRequest) {
        log.info("createNotification/request="+notificationRequest);
        Notification notification = Notification.builder()
                .sender(notificationRequest.getSender())
                .user(notificationRequest.getUser())
                .post(notificationRequest.getPost())
                .notificationType(notificationRequest.getNotificationType())
                .createdAt(LocalDateTime.now())
                .isRead(false).build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(long userId) {
        log.info("getNotificationsForUser/userId="+userId);

        return notificationRepository.findByUserId(userId);
    }

    public void markNotificationAsRead(long notificationId) {
        log.info("markNotificationAsRead/notificationId="+notificationId);

        Notification notification = findNotificationById(notificationId);
        notification.setRead(true);
        notificationRepository.save(notification);
    }


    private Notification findNotificationById(long notificationId) {
        log.info("findNotificationById/notificationId="+notificationId);
        return notificationRepository.findById(notificationId).orElseThrow(() -> new NoSuchElementException("notification not found"));
    }
}
