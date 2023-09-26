package com.appsdeveloperblog.shopify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@SpringBootApplication
public class ShopifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopifyApplication.class, args);
//		String message = "message for security check. ";
//		String subject = "code...";
//		String to = "abhiraj.kumar17362@marwadieducation.edu.in";
//		String from = "abhiraj20abc@gmail.com";
//
//		sendEmail(message, subject, to, from);
	}

//	private static void sendEmail(String message, String subject, String to, String from) {
//		//variable for gmail
//		String host = "smtp.gmail.com";
//
//		//get the system properties
//		Properties properties = System.getProperties();
//		System.out.println("PROPERTIES "+properties);
//
//		//setting importent information to properties object
//		//host set
//		properties.put("mail.smtp.host",host);
//		properties.put("mail.smtp.port","465");
//		properties.put("mail.smtp.ssl.enable","true");
//		properties.put("mail.smtp.auth","true");
//
//
//		//step1: to get the session object
//		Session session = Session.getInstance(properties, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication("abhiraj20abc@gmail.com","ithuygvvddrevbai");
//			}
//		});
//		session.setDebug(true);
//		//step2 compose the message
//		MimeMessage m = new MimeMessage(session);
//		try{
//			// from email
//			m.setFrom(from);
//
//			//adding recipent to message
//			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//			//adding subject to message
//			m.setSubject(subject);
//
//			//adding text to message
//			m.setText(message);
//
//			//send
//			//step 3 send the message using transport class
//			Transport.send(m);
//			System.out.println("sent success.......");
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
}
