package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderLineItems {
    private String title;
    private Double price;
    private Integer quantity;
//    private List<TaxLines> tax_lines;
}
