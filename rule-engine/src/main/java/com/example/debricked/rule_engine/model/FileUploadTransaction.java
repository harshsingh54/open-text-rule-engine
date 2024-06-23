package com.example.debricked.rule_engine.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FileUploadTransaction {

    private String email;
    private String version;
    private String repositoryName;
    private String commitName;
    private String branchName;

}
