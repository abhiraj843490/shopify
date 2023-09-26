package com.appsdeveloperblog.shopify.clients.service;

import com.appsdeveloperblog.shopify.clients.entity.email.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Session;

@Service
public class MailServiceImpl implements EmailService{
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendMail(EmailDetails details) {
        String host = "smtp.gmail.com";

		//get the system properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");


		//step1: to get the session object
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("abhiraj20abc@gmail.com","ithuygvvddrevbai");
			}
		});
		//step2 compose the message
		MimeMessage m = new MimeMessage(session);
		try{
			m.setFrom(details.getFrom());
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(details.getTo()));
			m.setSubject(details.getSubject());
			m.setText(details.getMessage());
			Transport.send(m);
			System.out.println("sent success.......");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return m.toString();
	}


}

