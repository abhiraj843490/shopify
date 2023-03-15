package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Customers {
    @Id
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
}
