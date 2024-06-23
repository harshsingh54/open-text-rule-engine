package com.example.debricked.rule_engine.model;


import lombok.Data;

@Data
public class UploadDependencyFilesResponse {

    private String ciUploadId;
    private String uploadProgramsFileId;
    private String totalScans;
    private String remainingScans;
    private String percentage;
    private String estimatedDaysLeft;

}
