package com.storelink.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Value ("${spring.mail.username}")
    private String sender;

    public void sendVerificationEmail(String to, String token) {
        
    	String subject = "Verify your email";
        String verificationUrl = "http://localhost:9090/api/auth/verify?token=" + token;

        String content = "<p>Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + verificationUrl + "\">Verify your email</a></p>";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(to);
		mailMessage.setText(content);
		mailMessage.setSubject(subject);
		
		System.out.println("Sending email....to: "+ to +". from:" +sender);
		
		mailSender.send(mailMessage);
    }
}
