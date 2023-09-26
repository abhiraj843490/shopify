package com.appsdeveloperblog.shopify.clients.service;

import com.appsdeveloperblog.shopify.clients.entity.email.EmailDetails;


public interface EmailService {
    String sendMail(EmailDetails emailDetails);
}
