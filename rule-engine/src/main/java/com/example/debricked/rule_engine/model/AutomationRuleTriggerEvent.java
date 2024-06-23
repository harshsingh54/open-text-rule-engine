package com.example.debricked.rule_engine.model;


import lombok.Data;

import java.util.List;

@Data
public class AutomationRuleTriggerEvent {

    private String dependency;
    private String dependencyLink;
    private List<String> licenses;
    private String cve;
    private String cveLink;
    private Integer cvss2;
    private Integer cvss3;
}
