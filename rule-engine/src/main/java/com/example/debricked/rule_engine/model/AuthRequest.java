package com.example.debricked.rule_engine.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthRequest {
    @JsonProperty("_username")
    private String username;
    @JsonProperty("_password")
    private String password;

}
