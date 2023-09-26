package com.appsdeveloperblog.shopify.clients.repository;

import com.appsdeveloperblog.shopify.clients.entity.fulfillmentResponse.FulFillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FulfillmentResRepository extends JpaRepository<FulFillment,Long> {
}
