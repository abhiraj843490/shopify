package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCustomer {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
}
