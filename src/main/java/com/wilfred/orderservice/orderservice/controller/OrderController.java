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
import java.util.Map;

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
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getList() {
        return orderService.getList();
    }

    @GetMapping("{orderNumber}")
    public OrderResponse getByOrderNumber(@PathVariable String orderNumber) {
        return orderService.getByOrderNumber(orderNumber);
    }

    @PutMapping("{id}")
    public OrderResponse update(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest);
    }

    @DeleteMapping("{id}")
    public Map<String, Object> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

}
