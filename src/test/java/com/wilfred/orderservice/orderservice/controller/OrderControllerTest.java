package com.wilfred.orderservice.orderservice.controller;

import com.wilfred.orderservice.orderservice.model.dto.OrderRequest;
import com.wilfred.orderservice.orderservice.model.dto.OrderResponse;
import com.wilfred.orderservice.orderservice.service.AbstractContainersTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderControllerTest  extends AbstractContainersTest {


    private static final String API_BASE_PATH = "/api/v1/orders";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void placeOrder() {
        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_006").skuCode("ORD_006").price(new BigDecimal(1000)).quantity(100).build();

        //when
        ResponseEntity<OrderResponse> orderResponse = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.POST, new HttpEntity<>(orderRequest), new ParameterizedTypeReference<OrderResponse>() {
        });

        //then
        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<List<OrderResponse>> orderResponseList = testRestTemplate.exchange(API_BASE_PATH + "/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderResponse>>() {
        });
        assertThat(orderResponseList.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponse response = Objects.requireNonNull(orderResponseList.getBody()).stream().filter(ord -> ord.getOrderNumber().equals(orderRequest.getOrderNumber())).findFirst().orElseThrow();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderNumber()).isEqualTo(orderRequest.getOrderNumber());


    }

    @Test
    void ping() {
        //given
        //when
        ResponseEntity<String> exchange = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });
        //then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isEqualTo("Ok");
    }

    @Test
    void getList() {

        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_007").skuCode("ORD_007").price(new BigDecimal(1000)).quantity(100).build();

        //when
        ResponseEntity<OrderResponse> createResponse = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.POST, new HttpEntity<>(orderRequest), new ParameterizedTypeReference<OrderResponse>() {
        });
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<List<OrderResponse>> orderResponseList = testRestTemplate.exchange(API_BASE_PATH + "/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderResponse>>() {
        });
        //then
        assertThat(orderResponseList.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponse response = Objects.requireNonNull(orderResponseList.getBody()).stream().filter(ord -> ord.getOrderNumber().equals(orderRequest.getOrderNumber())).findFirst().orElseThrow();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderNumber()).isEqualTo(orderRequest.getOrderNumber());

    }

    @Test
    void getByOrderNumber() {
        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_009").skuCode("ORD_009").price(new BigDecimal(1000)).quantity(100).build();

        //when
        ResponseEntity<OrderResponse> response = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.POST, new HttpEntity<>(orderRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse orderResponse = Objects.requireNonNull(response.getBody());
        ResponseEntity<OrderResponse> getOrderResponseByOrderNumber = testRestTemplate.exchange(API_BASE_PATH + "/" + orderResponse.getOrderNumber(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(getOrderResponseByOrderNumber.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponse orderByOrderNumber = Objects.requireNonNull(getOrderResponseByOrderNumber.getBody());
        assertThat(orderByOrderNumber.getOrderNumber()).isEqualTo(orderResponse.getOrderNumber());
        assertThat(orderByOrderNumber.getOrderNumber()).isEqualTo(orderRequest.getOrderNumber());


    }

    @Test
    void update() {
        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_010").skuCode("ORD_010").price(new BigDecimal(1000)).quantity(100).build();
        OrderRequest updateOrderRequest = OrderRequest.builder().orderNumber("ORD_012").skuCode("ORD_010").price(new BigDecimal(1000)).quantity(100).build();

        //when
        ResponseEntity<OrderResponse> response = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.POST, new HttpEntity<>(orderRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse orderResponse = Objects.requireNonNull(response.getBody());
        ResponseEntity<OrderResponse> updateResponse = testRestTemplate.exchange(API_BASE_PATH + "/" + orderResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateOrderRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponse updateOrder = Objects.requireNonNull(updateResponse.getBody());
        assertThat(updateOrder.getOrderNumber()).isEqualTo(updateOrderRequest.getOrderNumber());



    }

    @Test
    void deleteOrder() {
        //given
        OrderRequest orderRequest = OrderRequest.builder().orderNumber("ORD_008").skuCode("ORD_008").price(new BigDecimal(1000)).quantity(100).build();

        //when
        ResponseEntity<OrderResponse> createOrderResponse = testRestTemplate.exchange(API_BASE_PATH, HttpMethod.POST, new HttpEntity<>(orderRequest), new ParameterizedTypeReference<OrderResponse>() {
        });

        //then
        assertThat(createOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse orderResponse = Objects.requireNonNull(createOrderResponse.getBody());
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(API_BASE_PATH + "/" + orderResponse.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> deleteResponse = Objects.requireNonNull(response.getBody());
        assertThat(deleteResponse).containsKey("success");
        assertThat(deleteResponse).containsValue(true);

    }
}