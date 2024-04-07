package com.wilfred.orderservice.orderservice.repository;

import com.wilfred.orderservice.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
