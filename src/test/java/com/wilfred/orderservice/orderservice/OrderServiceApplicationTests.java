package com.wilfred.orderservice.orderservice;

import com.wilfred.orderservice.orderservice.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Disabled
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldCreateOrder() {
        String request = """
                               
                {
                  "orderNumber": "1",
                  "skuCode": "iPhone14",
                  "price": "1000",
                  "quantity": "100"
                }
                                
                                """;
        InventoryClientStub.stubInventoryCall("iPhone14", 100);
        RestAssured.given().contentType("application/json")
                .body(request).when()
                .post("/api/vi/orders")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("orderNumber", Matchers.equalTo("3"))
                .body("skuCode", Matchers.equalTo("iPhone14"))
                .body("price", Matchers.equalTo("1000"))
                .body("quantity", Matchers.equalTo("1000"));
        //.body("price", Matchers.equalTo("15000.0F"));

    }

    @Test
    void contextLoads() {
    }

}
