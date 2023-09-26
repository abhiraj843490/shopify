package com.appsdeveloperblog.shopify.clients.entity.createOrder;

import com.appsdeveloperblog.shopify.clients.entity.BillingAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrder {
    private List<OrderLineItems> line_items;
    private List<Transactions> transactions;
    private String currency= "EUR";
    private OrderCustomer customer;
    private ShippingAddress shipping_address;

    private String financial_status= "paid";
}
