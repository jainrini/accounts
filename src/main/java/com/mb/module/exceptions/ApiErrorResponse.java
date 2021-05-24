package com.mb.module.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    private HttpStatus status;
    private String error_code;
    private String message;
}
