package com.example.osbb.exceptions;

public class AuthException extends ApiException {

    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
