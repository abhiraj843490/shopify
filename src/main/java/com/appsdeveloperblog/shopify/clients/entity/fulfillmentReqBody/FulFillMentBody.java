package com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulFillMentBody {
    private String message = "The package was shipped this morning.";
    private List<LineIitemsByFulfillmentOrder> line_items_by_fulfillment_order;
}
