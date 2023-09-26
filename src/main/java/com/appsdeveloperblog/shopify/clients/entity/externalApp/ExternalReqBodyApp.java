package com.appsdeveloperblog.shopify.clients.entity.externalApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalReqBodyApp {
    private ExternalFulfillReq fulfillment_request;
}
