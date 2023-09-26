package com.appsdeveloperblog.shopify.clients.entity.fulfillmentService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePayload {
    	private String name;
                private String callback_url;
                private  Boolean inventory_management;
                private Boolean permits_sku_sharing;
                private Boolean fulfillment_orders_opt_in;
                private Boolean tracking_support;
                private Boolean requires_shipping_method;
                private String format;
}
