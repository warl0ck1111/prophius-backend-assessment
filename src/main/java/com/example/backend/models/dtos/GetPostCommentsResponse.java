package com.example.backend.models.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetPostCommentsResponse {
    private long commentId;
    private String comment;
    private LocalDateTime creationDate;
    private long PostId;
    private String postContent;
    private String username;
    private String email;

}

