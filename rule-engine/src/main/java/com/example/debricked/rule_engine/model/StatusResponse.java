package com.example.debricked.rule_engine.model;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StatusResponse {
    Map<String, StatusDependencyFile> dependencyFiles;
    private Integer progress;
    private Integer vulnerabilitiesFound;
    private Integer unaffectedVulnerabilitiesFound;
    private String automationsAction;
    private String policyEngineAction;
    private Boolean vulnerabilityOutputLimited;
    private List<AutomationRule> automationRules;
    private String detailsUrl;
}
