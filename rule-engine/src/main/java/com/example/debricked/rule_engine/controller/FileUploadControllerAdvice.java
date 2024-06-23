package com.example.debricked.rule_engine.controller;


import com.example.debricked.rule_engine.exception.DBException;
import com.example.debricked.rule_engine.exception.MissingRequiredArgumentException;
import com.example.debricked.rule_engine.model.ErrorResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class FileUploadControllerAdvice {

    @ExceptionHandler(value=SQLException.class)
    public ResponseEntity<ErrorResponseStatus> handleSQLExceptions(SQLException exception) throws DBException {
        log.error("SQL Exception", exception);
        ErrorResponseStatus errorResponseStatus= new ErrorResponseStatus("400", exception.getMessage());
        return new ResponseEntity<>(errorResponseStatus, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseStatus> handleIllegalArgumentExceptions(IllegalArgumentException exception) throws MissingRequiredArgumentException {
        log.error("Illegal Argument Exception : {}", exception.getMessage());
        ErrorResponseStatus errorResponseStatus= new ErrorResponseStatus("400", exception.getMessage());
        return new ResponseEntity<>(errorResponseStatus, HttpStatus.BAD_REQUEST);
    }

}
