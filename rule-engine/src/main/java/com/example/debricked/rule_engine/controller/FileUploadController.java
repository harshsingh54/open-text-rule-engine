package com.example.debricked.rule_engine.controller;


import com.example.debricked.rule_engine.model.DependencyFile;
import com.example.debricked.rule_engine.model.FileUploadTransaction;
import com.example.debricked.rule_engine.services.DebrickedService;
import com.example.debricked.rule_engine.services.FileProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

//    @PostMapping("/v2/upload")
//    public ResponseEntity<List<DependencyFile>> uploadFilesPostman(@RequestPart("files") MultipartFile[] files,
//                                                                   @RequestPart String email, @RequestPart String version,
//                                                                   @RequestPart String repositoryName, @RequestPart String commitName,
//                                                                   @RequestPart String branchName) {
//        log.info("Processing file : {}", files.length);
//        log.info("FileUploadTransaction : {}", email);
//        List<DependencyFile> processedFiles =
//                List.of(files).stream()
//                        .map(fileProcessingService::processFile)
//                        .collect(Collectors.toList());
//
//        return ResponseEntity.ok(processedFiles);
//    }
}
