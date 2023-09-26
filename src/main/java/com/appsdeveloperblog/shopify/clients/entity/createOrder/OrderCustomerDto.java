package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private List<ShippingAddress> addresses;
}
