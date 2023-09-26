package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderAddress {
    private Long id;
    private String first_name;
    private String last_name;
    private String address1;
    private String city;
    private String country;
}
