package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order placeOrder(OrderRequest orderRequest);

    List<OrderResponse> getList();

    OrderResponse getByOrderNumber(String orderNumber);

    OrderResponse updateOrder(Long id, OrderRequest orderRequest);

    Map<String, Object> deleteOrder(Long id);
}
