package com.appsdeveloperblog.shopify.clients.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderList {
    private List<Orders> orders;

}
