package com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FulFillMentLineItems {
    @Id
    private Long id;
    private Long shop_id;
    private Long fulfillment_order_id;
    private int quantity;
    private Long line_item_id;
    private Long inventory_item_id;
    private int fulfillable_quantity;
    private Long variant_id;
}
