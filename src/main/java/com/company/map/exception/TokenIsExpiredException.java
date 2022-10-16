package com.company.map.exception;

public class TokenIsExpiredException extends RuntimeException{

    public TokenIsExpiredException(String message) {
        super(message);
    }
}
