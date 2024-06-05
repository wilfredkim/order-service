package com.wilfred.orderservice.orderservice.service;

import com.wilfred.orderservice.orderservice.client.InventoryClient;
import com.wilfred.orderservice.orderservice.model.Order;
import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private InventoryClient inventoryClient;
    private OrderServiceImpl orderServiceImpl;
    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        orderServiceImpl = new OrderServiceImpl(orderRepository, inventoryClient, modelMapper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void placeOrder() {
        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_001").skuCode("ORD_001").price(new BigDecimal(1000)).quantity(100).build();
        //when
       // when(inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity())).thenReturn(true);
        when(orderRepository.save(any(Order.class))).
                thenReturn(Order.builder().orderNumber("ORD_001").skuCode("ORD_001").price(new BigDecimal(1000)).quantity(100).id(1L).build());
        Order actualResponse = orderServiceImpl.placeOrder(orderRequest);
        Order expectedResponse = Order.builder().orderNumber("ORD_001").skuCode("ORD_001").price(new BigDecimal(1000)).quantity(100).id(1L).build();
        //then
        assertThat(expectedResponse).isNotNull();
        assertThat(actualResponse.getOrderNumber()).isEqualTo(expectedResponse.getOrderNumber());
        assertThat(actualResponse.getId()).isEqualTo(1L);
    }

    @Test
    void getList() {
        //given
        //when
        orderServiceImpl.getList();
        //then
        verify(orderRepository).findAll();
    }

    @Test
    void testGetByOrderNumberShouldHaveValue() {
        //given
        String orderNumber = "ORD_001";
        Order order = Order.builder().orderNumber("ORD_001").skuCode("ORD_001").price(new BigDecimal(1000)).quantity(100).id(2L).build();
        //when
        when(orderRepository.findByOrderNumber(anyString())).thenReturn(Optional.of(order));
        OrderResponse actualOrderResponse = orderServiceImpl.getByOrderNumber(orderNumber);
        //then
        assertThat(actualOrderResponse.getOrderNumber()).isEqualTo(orderNumber);
        assertThat(actualOrderResponse.getSkuCode()).isEqualTo(order.getSkuCode());


    }

    @Test
    void testGetByOrderNumberShouldThrowARuntimeException() {
        //given
        String orderNumber = "ORD_001";
        //when
        when(orderRepository.findByOrderNumber(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> orderServiceImpl.getByOrderNumber(orderNumber)).isInstanceOf(RuntimeException.class)
                .hasMessage("Order with Order Number" + orderNumber + " does not exist");

    }

    @Test
    void updateOrder() {
        //given
        Long id = 3L;
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).build();
        Order order = Order.builder().orderNumber("ORD_002").skuCode("ORD_002").price(new BigDecimal(1000)).quantity(100).id(3L).build();
        //when
        when(orderRepository.findById(eq(id))).thenReturn(Optional.of(order));
        when(orderRepository.findByOrderNumber(eq(orderRequest.getOrderNumber()))).thenReturn(Optional.empty());//not taken
        when(orderRepository.save(any(Order.class))).
                thenReturn(Order.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).id(3L).build());
        //then
        OrderResponse actualOrderResponse = orderServiceImpl.updateOrder(id, orderRequest);
        assertThat(actualOrderResponse.getId()).isEqualTo(id);
        assertThat(actualOrderResponse.getOrderNumber()).isEqualTo(orderRequest.getOrderNumber());
        assertThat(actualOrderResponse.getSkuCode()).isEqualTo(orderRequest.getSkuCode());

    }

    @Test
    void testUpdateOrderWhenOrderNumberAlreadyExists() {
        //given
        Long id = 3L;
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).build();
        Order order = Order.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).id(3L).build();
        //when
        when(orderRepository.findById(eq(id))).thenReturn(Optional.of(order));
        when(orderRepository.findByOrderNumber(eq(orderRequest.getOrderNumber()))).thenReturn(Optional.of(order));// taken
        //then
        assertThatThrownBy(() -> orderServiceImpl.updateOrder(id, orderRequest))
                .isInstanceOf(RuntimeException.class).hasMessage("Order Number   " + orderRequest.getOrderNumber() + " has already been taken");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testUpdateOrderWhenOrderDoesNotExists() {
        //given
        Long id = 3L;
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).build();
        Order order = Order.builder().orderNumber("ORD_003").skuCode("ORD_003").price(new BigDecimal(1000)).quantity(100).id(3L).build();
        //when
        when(orderRepository.findById(eq(id))).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> orderServiceImpl.updateOrder(id, orderRequest))
                .isInstanceOf(RuntimeException.class).hasMessage("Order with id " + id + " does not exist");
        verify(orderRepository, never()).save(any());
        verify(orderRepository, never()).findByOrderNumber(anyString());
    }

    @Test
    void deleteOrder() {
        //given
        Long id = 1L;
        //when
        Map<String, Object> deleteOrder = orderServiceImpl.deleteOrder(id);
        verify(orderRepository).deleteById(id);
        //then
        assertThat(deleteOrder).containsKey("success");
        assertThat(deleteOrder).containsValue(true);

    }


}