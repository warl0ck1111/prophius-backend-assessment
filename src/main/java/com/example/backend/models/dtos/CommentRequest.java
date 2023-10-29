package com.example.backend.models.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "content can not be empty")
    private String content;
    @NotBlank(message = "post id can not be empty")
    private long postId;
     @NotBlank(message = "user id can not be empty")
    private long userId;


}
