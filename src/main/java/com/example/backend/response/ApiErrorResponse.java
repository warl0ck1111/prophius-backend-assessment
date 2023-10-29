package com.example.backend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Okala Bashir .O.
 * created at 18/10/2020
 */

@Data
@Builder
@AllArgsConstructor


/**
 * Used when An error occurred in processing the request, i.e. an exception was thrown (usually internal server errors 500)
 */

public class ApiErrorResponse implements Serializable {

    private final String status = "error";
    private String message;


    
}
