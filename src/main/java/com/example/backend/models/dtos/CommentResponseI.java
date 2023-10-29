package com.example.backend.models.dtos;

import java.time.LocalDateTime;

public interface CommentResponseI {
    Long getCommentId();
    String getContent();
    Long getUserId();
    LocalDateTime getCreationDate();
}
