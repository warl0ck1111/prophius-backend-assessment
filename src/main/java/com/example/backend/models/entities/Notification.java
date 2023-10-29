package com.example.backend.models.entities;

import com.example.backend.models.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Recipient of the notification

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender; // Sender of the notification

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post; // Associated post

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // E.g., 'like' or 'comment'

    private boolean isRead;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime updatedAt;



}
