package com.wilfred.orderservice.orderservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
public class OrderRequest {
    Long id;
    @NotEmpty String orderNumber;
    @NotEmpty String skuCode;
    @NotNull BigDecimal price;
    Integer quantity;
}
