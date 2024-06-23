package com.example.debricked.rule_engine.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AutomationRuleTriggerEvent {

    private String dependency;
    private String dependencyLink;
    private List<String> licenses;
    private String cve;
    private String cveLink;
    private Integer cvss2;
    private Integer cvss3;
}
