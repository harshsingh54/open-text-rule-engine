package com.example.debricked.rule_engine.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseStatus {

    private String status;
    private String message;

}
