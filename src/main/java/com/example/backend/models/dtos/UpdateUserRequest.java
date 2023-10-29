package com.example.backend.models.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "username can not be empty")
    private String username;
    @Email(message = "invalid email")
    private String email;

}
