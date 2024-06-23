package com.example.debricked.rule_engine.model;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatusDependency {

    private String dependencyName;
    private String version;

}
