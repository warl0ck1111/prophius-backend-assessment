package com.example.backend.models.dtos;

import com.example.backend.models.enums.NotificationType;
import lombok.Data;


@Data

public class CreateNotificationRequest {
    private Long id;

    private long userId; // Recipient of the notification

    private long senderId; // Sender of the notification

    private long postId; // Associated post (if applicable)

    private NotificationType notificationType; // E.g., 'like' or 'comment'


}
