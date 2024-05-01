package com.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

	@Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //@Async
    public void sendEmail(SimpleMailMessage email) {
    	System.out.println("Email content is : " + email);
    	//email.setFrom("thanniru.bhanusaisree@gmail.com");
    	System.out.println("Email content is : " + email);
        javaMailSender.send(email);
    }

}