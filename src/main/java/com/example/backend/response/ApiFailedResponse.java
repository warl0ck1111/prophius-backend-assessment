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
 * Used when There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied (usually a client error 400)
 */
public class ApiFailedResponse implements Serializable {

    private final String status = "fail";
        
    private Object data;


}
