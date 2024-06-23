package com.example.debricked.rule_engine.model;


import lombok.Data;

@Data
public class QueueFilesRequest {

    private String ciUploadId;
    private String repositoryName;
    private String commitName;
    private String returnCommitData;
//    private byte[] repositoryZip;

}
