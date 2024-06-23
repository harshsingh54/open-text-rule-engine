package com.example.debricked.rule_engine.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AutomationRule {

    private String ruleDescription;
    private List<String> ruleActions;
    private String ruleLink;
    private Boolean hasCves;
    private Boolean triggered;
    private List<AutomationRuleTriggerEvent> triggerEvents;

}
