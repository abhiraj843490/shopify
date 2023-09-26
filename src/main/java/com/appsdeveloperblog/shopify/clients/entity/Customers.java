package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Entity
public class Customers {
    @Id
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private List<Addresses> addresses;
}
