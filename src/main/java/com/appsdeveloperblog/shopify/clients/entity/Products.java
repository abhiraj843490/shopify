package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Entity
public class Products {
    @Id
    private Long id;
    private String title;
    private String vendor;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Variants>variants;

    public Products(Long id, String title, String vendor) {
        this.id = id;
        this.title = title;
        this.vendor = vendor;
    }
    public Products() {
    }
}
