package com.example.backend.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String role;
    private String userId;
    private String username;
    private String email;
    private String refreshToken;
}
