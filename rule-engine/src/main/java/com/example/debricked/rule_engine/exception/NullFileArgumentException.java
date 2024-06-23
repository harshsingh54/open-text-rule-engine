package com.example.debricked.rule_engine.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "'files' cannot be null")
public class NullFileArgumentException extends RuntimeException{

    public NullFileArgumentException(){}

    public NullFileArgumentException(String message){
        super(message);
    }
}
