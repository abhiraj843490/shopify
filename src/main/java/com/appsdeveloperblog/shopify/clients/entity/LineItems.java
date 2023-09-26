package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItems {
    @Id
    private Long id;
    private String fulfillment_status;
    private String fulfillment_service;
    private String name;
    private Long product_id;
}
