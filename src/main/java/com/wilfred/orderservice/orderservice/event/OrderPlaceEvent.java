package com.wilfred.orderservice.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderPlaceEvent {
    private String orderNumber;
    private String email;
}
