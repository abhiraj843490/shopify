package com.appsdeveloperblog.shopify.clients.entity.fulfillmentResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FulFillment {
    @Id
    private Long id;
    private String name;
    private Long order_id;
    private String service;
    private String status;
}
