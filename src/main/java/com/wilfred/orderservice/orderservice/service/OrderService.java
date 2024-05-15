package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderService {

    Order placeOrder(OrderRequest orderRequest);

    List<OrderResponse> getList();
}
