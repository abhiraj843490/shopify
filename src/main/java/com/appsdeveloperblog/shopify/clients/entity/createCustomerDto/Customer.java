package com.appsdeveloperblog.shopify.clients.entity.createCustomerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Address> addresses;

    public Customer(String firstName, String lastName, String email) {

    }
}
