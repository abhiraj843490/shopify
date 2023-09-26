package com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineIitemsByFulfillmentOrder {
    private Long fulfillment_order_id;
    private List<FulfillmentOrderLineItems> fulfillment_order_line_items;
}
