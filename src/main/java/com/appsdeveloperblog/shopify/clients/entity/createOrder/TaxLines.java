package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaxLines {
    private Double price=13.5;
    private Double rate=0.02;
    private String title="State tax";
}
