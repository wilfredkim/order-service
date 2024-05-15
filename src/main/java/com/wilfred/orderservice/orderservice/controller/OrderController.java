package com.wilfred.orderservice.orderservice.controller;

import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/orders")
@RestController
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("Here::::::::::::");
        var order = orderService.placeOrder(orderRequest);
        return modelMapper.map(order, OrderResponse.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        return "Ok";
    }

    @GetMapping("/all")
    public List<OrderResponse> getList() {
        return orderService.getList();
    }

}
