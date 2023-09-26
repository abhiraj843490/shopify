package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BillingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String first_name;
    private String last_name;
    private String address1;
    private String phone;
    private String city;
    private String zip;
    private String province;
    private String country;
    private String name;

}
