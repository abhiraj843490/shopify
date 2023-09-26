package com.appsdeveloperblog.shopify.clients.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    public Long id;
    public String name;
    public String contact_email;
    public String email;
    private String financial_status;
    private String fulfillment_status;
    private String source_name;
    private String total_price;
    private Long user_id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<LineItems>line_items;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Customers customer;
    @OneToOne(cascade = CascadeType.ALL)
    private BillingAddress billing_address;

}
