package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Variants {
    @Id
    private Long id;
    private Long product_id;
    private String title;
    private double price;
    private Long position;

}
