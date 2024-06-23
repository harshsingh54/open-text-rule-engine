package com.example.debricked.rule_engine.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class StatusDependencyFile {

    private String fileName;
    private String vulnerabilitiesCount;
    private String progress;
    private List<StatusDependencyVulnerability> vulnerabilities;
}
