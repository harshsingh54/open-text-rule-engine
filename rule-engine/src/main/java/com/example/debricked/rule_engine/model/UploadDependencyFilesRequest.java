package com.example.debricked.rule_engine.model;


import lombok.Data;

@Data
public class UploadDependencyFilesRequest {

    private byte[] fileData;
    private String fileName;
    private String fileRelativePath;
    //Required
    private String repositoryName;
    //Required
    private String commitName;
    private String repositoryUrl;
    private String branchName;
    private String defaultBranchName;
    private String ciUploadId;

}
