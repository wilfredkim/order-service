package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.client.InventoryClient;
import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public Order placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = Order.builder()
                    .orderNumber(orderRequest.orderNumber())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();
            return orderRepository.save(order);
        } else
            throw new RuntimeException("Product with skucode " + orderRequest.orderNumber() + " is not in stock!");
    }
}
