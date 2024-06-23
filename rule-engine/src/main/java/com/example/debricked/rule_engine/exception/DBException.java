package com.example.debricked.rule_engine.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR, reason = "Database error occurred")
public class DBException extends SQLException {
}
