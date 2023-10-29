package com.example.backend.models.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;



@Data
@Builder
public class CreatePostResponse {


    private Long postId;
    private String content;
    private int likesCount;

    private long userId;
    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime creationDate;
}
