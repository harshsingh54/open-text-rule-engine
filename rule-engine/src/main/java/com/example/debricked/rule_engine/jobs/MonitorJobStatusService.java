package com.example.debricked.rule_engine.jobs;


import com.example.debricked.rule_engine.model.DependencyFile;
import com.example.debricked.rule_engine.services.FileProcessingService;
import com.example.debricked.rule_engine.services.RuleEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MonitorJobStatusService {

    private FileProcessingService fileProcessingService;
    private RuleEvaluationService ruleEvaluationService;

    @Autowired
    public MonitorJobStatusService(FileProcessingService fileProcessingService, RuleEvaluationService ruleEvaluationService){
        this.fileProcessingService = fileProcessingService;
        this.ruleEvaluationService=ruleEvaluationService;
    }

    @Scheduled(fixedRateString = "60000")
    public void monitorUploadJobStatus(){
        log.info("Job started checking In Progress Jobs ...");

        List<DependencyFile> dependencyFileList =fileProcessingService.getAllInProgressFiles();

        log.info("Total InProgress jobs : {}", dependencyFileList.size());

        Map<String, List<DependencyFile>> resultMap = dependencyFileList.stream()
                .collect(Collectors.groupingBy(DependencyFile::getCiUploadId));

        ruleEvaluationService.evaluateRulesAndNotifyUsers(resultMap);
    }

}
