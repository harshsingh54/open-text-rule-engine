package com.example.debricked.rule_engine.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="file_details")
public class DependencyFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String filename;
    private String version;
    private String repositoryName;
    private String commitName;
    private String branchName;
    private String ciUploadId;
    @Lob
    private byte[] file;
    private String scanStatus; // e.g., "SUCCESS" or "ERROR"

}