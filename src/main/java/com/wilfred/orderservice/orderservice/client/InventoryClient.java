package com.wilfred.orderservice.orderservice.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    @GetExchange(value = "/api/v1/inventories")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);


}
