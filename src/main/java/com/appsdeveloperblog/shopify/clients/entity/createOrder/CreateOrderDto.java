package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    private List<OrderLineItems> line_items;
    private List<Transactions> transactions;
    private String currency= "EUR";
    private List<OrderCustomerDto> customer;
    private String financial_status= "paid";

}
