package com.appsdeveloperblog.shopify.clients.entity.createCustomerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Long id;
    private Long customer_id;

    private String first_name;
    private String last_name;
    private String address1;
    private String city;
    private String country;
}
