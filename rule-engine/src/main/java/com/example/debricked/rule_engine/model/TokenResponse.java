package com.example.debricked.rule_engine.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
