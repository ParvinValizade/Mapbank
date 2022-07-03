package com.company.map.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BalanceNotEnoughException extends RuntimeException{
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}
