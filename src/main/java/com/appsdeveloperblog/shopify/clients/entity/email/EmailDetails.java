package com.appsdeveloperblog.shopify.clients.entity.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String message;
    private String subject;
    private String to;
    private String from;
}
