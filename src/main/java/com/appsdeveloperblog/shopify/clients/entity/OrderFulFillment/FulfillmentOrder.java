package com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FulfillmentOrder {
    @Id
    private Long id;
    private Long shop_id;
    @Column(name = "order_id")
    private Long order_id;
    private String request_status;
    private String status;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fulfillment_order_id")
    private List<FulFillMentLineItems>line_items;

}
