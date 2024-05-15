package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.client.InventoryClient;
import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryClient inventoryClient, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.modelMapper = modelMapper;
    }

    public Order placeOrder(OrderRequest orderRequest) {
        log.info("SkuCode::::::::::::::::" + orderRequest.skuCode());
        log.info("Quantity::::::::::::::::" + orderRequest.quantity());

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

    @Override
    public List<OrderResponse> getList() {
        var orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class)).toList();

    }
}
