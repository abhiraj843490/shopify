package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Addresses {
    @Id
    private Long id;
    private Long customer_id;
    private String first_name;
    private String last_name;
    private String address1;
    private String city;
    private String country;
}
