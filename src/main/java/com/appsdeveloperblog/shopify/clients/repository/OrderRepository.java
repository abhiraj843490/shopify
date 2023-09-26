package com.appsdeveloperblog.shopify.clients.repository;

import com.appsdeveloperblog.shopify.clients.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
}
