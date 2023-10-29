package com.example.backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Okala Bashir .O.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    private String email;
    private String token;
    private String password;
    private String confirmPassword;
}
