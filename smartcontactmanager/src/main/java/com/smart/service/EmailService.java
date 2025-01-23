package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message, String to){
        boolean flag = false;

        String from = "priyanshubaral9562@gmail.com";

        //smtp Properties
        Properties properties = System.getProperties();
        String host = "smtp.gmail.com";

        properties.put("mail.smtp.host", host); // It uses mail.smtp.host as a key to get the value
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true"); // It is enabled for security purpose (security socket number)
        properties.put("mail.smtp.auth", "true");

        String username = "priyanshubaral9562@gmail.com";
        String password = "vfpa xgug rvfo ziwp";

        //Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.getDebug();

        Message m = new MimeMessage(session);

        try {

        	m.setFrom(new InternetAddress(from));
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
			/* m.setText(message); */
            
            m.setContent(message, "text/html");

            Transport.send(m);

            flag = true;

        } catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }

}
