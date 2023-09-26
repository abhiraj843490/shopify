package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    private Long id;
    private String title;
    private String vendor;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Variants>variants;

}
