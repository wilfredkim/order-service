package com.wilfred.orderservice.orderservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(Long id, @NotEmpty String orderNumber, @NotEmpty String skuCode, @NotNull BigDecimal price,
                           Integer quantity) {
}
