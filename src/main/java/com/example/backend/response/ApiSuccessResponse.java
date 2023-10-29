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
 * used when All went well, and (usually) some data was returned.(2XX)
 *
 */
public class ApiSuccessResponse implements Serializable {

    private final String status = "success";
        
    private Object data;


    
}
