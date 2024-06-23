package com.example.debricked.rule_engine.controller;


import com.example.debricked.rule_engine.exception.NullFileArgumentException;
import com.example.debricked.rule_engine.model.DependencyFile;
import com.example.debricked.rule_engine.model.FileUploadTransaction;
import com.example.debricked.rule_engine.services.DebrickedService;
import com.example.debricked.rule_engine.services.FileProcessingService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.base.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private FileProcessingService fileProcessingService;
    @Autowired
    private DebrickedService debrickedService;

    @PostMapping("/v1/upload")
    public ResponseEntity<List<DependencyFile>> uploadFiles(@RequestPart("files") MultipartFile[] files,
                                                            FileUploadTransaction fileUploadTransaction) {
//        Preconditions
        Preconditions.checkArgument(StringUtils.isNotBlank(fileUploadTransaction.getEmail()), "Email required to notify");

        Preconditions.checkArgument(StringUtils.isNotBlank(fileUploadTransaction.getRepositoryName()), "'repositoryName' is a required field");

        Preconditions.checkArgument(StringUtils.isNotBlank(fileUploadTransaction.getCommitName()), "'commitName' is a required field");

        if(files==null){
            throw new NullFileArgumentException();
        }

        log.info("Processing file : {}", files.length);
        log.info("FileUploadTransaction : {}", fileUploadTransaction);

        List<DependencyFile> resultList =new ArrayList<>();
        List<Integer> idList= new ArrayList<>();

        for(MultipartFile file: files){
            DependencyFile dependencyFile=fileProcessingService.processFile(file, fileUploadTransaction);
            resultList.add(dependencyFile);
            idList.add(dependencyFile.getId());
        }

        debrickedService.processFiles(idList);

        return ResponseEntity.ok(resultList);
    }

}
