package com.wilfred.orderservice.orderservice.repository;

import com.wilfred.orderservice.orderservice.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));
    @Autowired
    private OrderRepository orderRepository;


    @Test
    void canEstablishConnection() {
        assertThat(mySQLContainer.isCreated()).isTrue();
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        Order order = Order.builder().orderNumber("ORD_001").skuCode("ORD_001").price(new BigDecimal(1000)).quantity(100).build();
        orderRepository.save(order);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldFindByOrderNumber() {
        //given
        //when
        Optional<Order> ord001 = orderRepository.findByOrderNumber("ORD_001");
        //then
        assertThat(ord001).isPresent();
    }

    @Test
    void shouldNotFindByOrderNumber() {
        //given
        //when
        Optional<Order> ord001 = orderRepository.findByOrderNumber("ORD_002");
        //then
        assertThat(ord001).isNotPresent();
    }


}