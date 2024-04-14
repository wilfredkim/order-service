package com.wilfred.orderservice.orderservice.controller;

import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Here::::::::::::");
        var order = orderService.placeOrder(orderRequest);
        return OrderResponse.
                builder().
                id(order.getId()).orderNumber(order.getOrderNumber())
                .price(order.getPrice()).skuCode(order.getSkuCode())
                .quantity(order.getQuantity()).
                build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        return "Ok";
    }

}
