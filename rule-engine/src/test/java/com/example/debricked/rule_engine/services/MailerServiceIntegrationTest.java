package com.example.debricked.rule_engine.services;


import com.example.debricked.rule_engine.commons.Constants;
import com.example.debricked.rule_engine.model.AutomationRule;
import com.example.debricked.rule_engine.model.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class MailerServiceIntegrationTest {

    @Autowired
    MailerService mailerService;
    @Autowired
    RuleEvaluationService ruleEvaluationService;

    @Test
    public void testEmailServices() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/static/sample.json");
        StatusResponse response = objectMapper.readValue(file, StatusResponse.class);
        System.out.println("Status :\n" + response);
        List<AutomationRule> automationRuleList= response.getAutomationRules();

        List<AutomationRule> filteredRules= automationRuleList.stream().filter(AutomationRule::getTriggered)
                .filter(automationRule -> automationRule.getRuleActions().contains(Constants.RULE_ACTION_SEND_EMAIL))
                .collect(Collectors.toList());

        List<String> messages=ruleEvaluationService.generateMessagesFromTriggerEvents(filteredRules);
        List<String> emailList = List.of("abc@xyz.com", "def@xyz.com");

        mailerService.sendEmailToOwners(emailList, messages);

    }

}
