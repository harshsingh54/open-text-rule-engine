package com.example.debricked.rule_engine.services;

import com.example.debricked.rule_engine.model.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/static/sample.json");
        StatusResponse response = objectMapper.readValue(file, StatusResponse.class);
    }
}
