package com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulFillmentDto {
    private List<FulfillmentOrder>fulfillment_orders;
}
