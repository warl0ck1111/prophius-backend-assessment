package com.example.backend.models.dtos;

import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.NotificationType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateNotificationRequest {
    private Long id;

    private User user; // Recipient of the notification

    private User sender; // Sender of the notification

    private Post post; // Associated post (if applicable)

    private NotificationType notificationType; // E.g., 'like' or 'comment'


}
