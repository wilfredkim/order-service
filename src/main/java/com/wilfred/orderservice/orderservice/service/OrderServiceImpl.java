package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.client.InventoryClient;
import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info("SkuCode::::::::::::::::" + orderRequest.getSkuCode());
        log.info("Quantity::::::::::::::::" + orderRequest.getQuantity());

        var isProductInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());
        if (isProductInStock) {
            Order order = Order.builder()
                    .orderNumber(orderRequest.getOrderNumber())
                    .price(orderRequest.getPrice())
                    .skuCode(orderRequest.getSkuCode())
                    .quantity(orderRequest.getQuantity())
                    .build();
            return orderRepository.save(order);
        } else
            throw new RuntimeException("Product with skucode " + orderRequest.getOrderNumber() + " is not in stock!");
    }

    @Override
    public List<OrderResponse> getList() {
        var orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class)).toList();

    }

    @Override
    public OrderResponse getByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order with Order Number" + orderNumber + " does not exist"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        // find order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with id " + id + " does not exist"));
        // check if the department name exists
        boolean orderNumberExists = orderRepository.findByOrderNumber(orderRequest.getOrderNumber())
                .isPresent();
        if (orderNumberExists) {
            throw new RuntimeException(
                    "Order Number   " + order.getOrderNumber() + " has already been taken");
        }
        // update the field of department name if it does not exist
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setSkuCode(orderRequest.getSkuCode());
        order.setPrice(orderRequest.getPrice());
        order.setQuantity(orderRequest.getQuantity());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    @Override
    public Map<String, Object> deleteOrder(Long id) {
        orderRepository.deleteById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }


}
