package com.example.debricked.rule_engine.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason="File data is empty")
public class EmptyFileArgumentException extends RuntimeException{

    public EmptyFileArgumentException(){}

    public EmptyFileArgumentException(String message){
        super(message);
    }

}
