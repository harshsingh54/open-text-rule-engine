package com.example.debricked.rule_engine.services;


import com.example.debricked.rule_engine.commons.Constants;
import com.example.debricked.rule_engine.model.AutomationRule;
import com.example.debricked.rule_engine.model.DependencyFile;
import com.example.debricked.rule_engine.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RuleEvaluationService {

    private DebrickedService debrickedService;
    private MailerService mailerService;

    public RuleEvaluationService(DebrickedService debrickedService, MailerService mailerService){
        this.debrickedService = debrickedService;
        this.mailerService= mailerService;
    }

    public void evaluateRulesAndNotifyUsers(Map<String, List<DependencyFile>> uploadIdMap){

        for(Map.Entry<String, List<DependencyFile>> entry: uploadIdMap.entrySet()){
            StatusResponse statusResponse = debrickedService.getResponseForUploadId(entry.getKey());

            if(statusResponse==null){
                continue;
            }

            List<AutomationRule> automationRuleList= statusResponse.getAutomationRules();

            List<AutomationRule> filteredRules= automationRuleList.stream().filter(AutomationRule::getTriggered)
                    .filter(automationRule -> automationRule.getRuleActions().contains(Constants.RULE_ACTION_SEND_EMAIL))
                    .collect(Collectors.toList());

            List<String> messages = generateMessagesFromTriggerEvents(filteredRules);
            List<String> emailList = entry.getValue().stream().map(DependencyFile::getEmail).distinct()
                    .collect(Collectors.toList());
            if(messages!=null && !messages.isEmpty()){
                mailerService.sendEmailToOwners(emailList, messages);
            }else{
                log.info("No alerts to be sent for upload id : {}", entry.getKey());
            }
        }
    }

    public List<String> generateMessagesFromTriggerEvents(List<AutomationRule> automationRules){
        List<String> messages = new ArrayList<>();

        for (AutomationRule rule:automationRules) {
            String description = String.format("Rule : %s", rule.getRuleDescription());
            messages.add(description);
            messages.add("\nReported Vulnerabilities : ");
            List<String> cveMsgs = rule.getTriggerEvents().stream()
                    .map(event-> String.format("\t* %s reported for dependency : %s",event.getCve(), event.getDependency()))
                    .collect(Collectors.toList());

            if (cveMsgs!=null){
                System.out.println("CVE msg : {}"+ cveMsgs.get(0));
                messages.addAll(cveMsgs);
                messages.add("\n");
            }

        }

        return messages;
    }

}
