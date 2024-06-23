package com.example.debricked.rule_engine.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Request missing required arguments")
public class MissingRequiredArgumentException extends RuntimeException {

    public MissingRequiredArgumentException(){}

    public MissingRequiredArgumentException(String message){
        super(message);
    }

}
