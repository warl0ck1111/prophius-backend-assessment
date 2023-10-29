package com.example.backend.models.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UpdateCommentRequest {
    @NotBlank(message = "content can not be empty")
    private String content;

    @NotNull(message = "user id can not be null")
    private Long userId;
}
