package com.mb.module.exceptions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(AccountNotFoundException exception, WebRequest request) {
        ApiErrorResponse apiResponse = new ApiErrorResponse
            .ApiErrorResponseBuilder()
            .status(HttpStatus.NOT_FOUND)
            .error_code("404")
            .message(exception.getLocalizedMessage())
            .build();
        return new ResponseEntity(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    protected ResponseEntity<Object> validationErrorHandling(BindException ex) {
        List<String> errorMsg = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getDefaultMessage())
            .collect(Collectors.toList());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse
            .ApiErrorResponseBuilder()
            .status(HttpStatus.BAD_REQUEST)
            .error_code("400")
            .message(errorMsg.toString())
            .build();
        return new ResponseEntity(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @ExceptionHandler(MismatchedInputException.class)
    protected ResponseEntity<Object> validationErrorHandling(MismatchedInputException ex) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse
            .ApiErrorResponseBuilder()
            .status(HttpStatus.BAD_REQUEST)
            .error_code("400")
            .message(ex.getOriginalMessage())
            .build();
        return new ResponseEntity(apiErrorResponse, apiErrorResponse.getStatus());
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> validationErrorHandling(Exception ex) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse
            .ApiErrorResponseBuilder()
            .status(HttpStatus.BAD_GATEWAY)
            .error_code("500")
            .message(ex.getMessage())
            .build();
        return new ResponseEntity(apiErrorResponse, apiErrorResponse.getStatus());
    }
}
