package com.wilfred.orderservice.orderservice.model.dto;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record OrderRequest(Long id, @NotEmpty String orderNumber, @NotEmpty String skuCode, @NotEmpty BigDecimal price,
                           Integer quantity) {
}
