package com.example.debricked.rule_engine.services;


import com.example.debricked.rule_engine.commons.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MailerService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constants.EMAIL_FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }



    public void sendEmailToOwners(List<String> ownerEmails, List<String> messages){

        log.info("Owner Emails :{}", ownerEmails);
        log.info("Messages : \n{}", messages);


        String init = "Following vulnerabilities has been reported in your project based on following rules.\n\n";
        String text = init+ String.join("\n",messages);

        for(String email: ownerEmails){
            sendSimpleMessage(email, Constants.EMAIL_SUBJECT, text);
        }

    }

}
