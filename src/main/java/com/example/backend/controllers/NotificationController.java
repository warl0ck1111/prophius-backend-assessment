package com.example.backend.controllers;


import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.entities.Notification;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.services.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Create a new notification for test todo: remove API later
    @PostMapping("/")
    public ResponseEntity<Notification> createNotification(@RequestBody @Valid CreateNotificationRequest notificationRequest) {
        Notification newNotification = notificationService.createNotification(notificationRequest);
        return ResponseEntity.ok(newNotification);
    }

    // Get notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PutMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.info("handleException/");
        log.error("handleException/e="+e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
