package com.example.debricked.rule_engine.model;


import lombok.Data;

import java.util.List;

@Data
public class StatusDependencyFile {

    private String fileName;
    private String vulnerabilitiesCount;
    private String progress;
    private List<StatusDependencyVulnerability> vulnerabilities;
}
