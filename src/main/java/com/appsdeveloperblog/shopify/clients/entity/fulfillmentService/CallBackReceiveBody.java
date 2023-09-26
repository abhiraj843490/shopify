package com.appsdeveloperblog.shopify.clients.entity.fulfillmentService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallBackReceiveBody {
    private String kind="fulfillment_request";

}
