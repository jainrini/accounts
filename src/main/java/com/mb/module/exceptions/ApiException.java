package com.mb.module.exceptions;

public class ApiException extends RuntimeException {
public ApiException(String message){
    super(message);
}
}
