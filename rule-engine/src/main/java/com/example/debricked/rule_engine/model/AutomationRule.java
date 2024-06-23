package com.example.debricked.rule_engine.model;


import lombok.Data;
import java.util.List;

@Data
public class AutomationRule {

    private String ruleDescription;
    private List<String> ruleActions;
    private String ruleLink;
    private Boolean hasCves;
    private Boolean triggered;
    private List<AutomationRuleTriggerEvent> triggerEvents;

}
