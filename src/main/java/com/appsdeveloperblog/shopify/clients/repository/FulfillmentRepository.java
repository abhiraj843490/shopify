package com.appsdeveloperblog.shopify.clients.repository;

import com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment.FulfillmentOrder;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody.LineIitemsByFulfillmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FulfillmentRepository extends JpaRepository<FulfillmentOrder, Long> {
//    FulfillmentOrder findByOrder_id(Long id);

}

