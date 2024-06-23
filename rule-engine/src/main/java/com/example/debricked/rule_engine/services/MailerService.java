package com.example.debricked.rule_engine.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MailerService {



    public void sendEmailToOwners(List<String> owners, List<String> messages){

        log.info("Owner Emails :{}", owners);
        log.info("Messages : \n{}", messages);

    }

}
