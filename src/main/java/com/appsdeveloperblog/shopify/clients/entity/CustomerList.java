package com.appsdeveloperblog.shopify.clients.entity;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerList {
    public List<Customers> customers;
}
