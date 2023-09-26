package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transactions {
    private String kind="sale";
    private String status= "success";
    private Double amount;
}
