package com.example.backend.models.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "content can not be empty")
    private String content;
    @NotNull(message = "post id can not be empty")
    private Long postId;
     @NotNull(message = "user id can not be empty")
    private Long userId;


}
