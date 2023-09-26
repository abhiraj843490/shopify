package com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfillmentOrderLineItems {
        private Long id;
        private int quantity;
}
