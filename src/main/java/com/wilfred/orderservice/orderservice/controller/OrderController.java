package com.wilfred.orderservice.orderservice.controller;

import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
        var order = orderService.placeOrder(orderRequest);
        return OrderResponse.
                builder().
                id(order.getId()).orderNumber(order.getOrderNumber())
                .price(order.getPrice()).skuCode(order.getSkuCode())
                .quantity(order.getQuantity()).
                build();
    }


}
