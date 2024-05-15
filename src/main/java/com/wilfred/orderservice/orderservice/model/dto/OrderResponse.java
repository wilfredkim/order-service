package com.wilfred.orderservice.orderservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {
    Long id;
    String orderNumber;
    String skuCode;
    BigDecimal price;
    Integer quantity;
}
