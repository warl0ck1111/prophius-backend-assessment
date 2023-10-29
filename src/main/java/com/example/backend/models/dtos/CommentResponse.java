package com.example.backend.models.dtos;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private long commentId;
    private String content;
    private long userId;
    private LocalDateTime creationDate;



}
