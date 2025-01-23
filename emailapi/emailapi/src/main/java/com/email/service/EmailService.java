package com.email.service;

import java.io.File;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public boolean sendEmail(String subject, String message, String to, File file){
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

            m.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setFrom(new InternetAddress(from));
            m.setSubject(subject);

            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText(message);

            MimeBodyPart part2 = new MimeBodyPart();
            part2.attachFile(file);

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(part1);
            mimeMultipart.addBodyPart(part2);

            m.setContent(mimeMultipart);

            Transport.send(m);

            flag = true;

        } catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }
}
