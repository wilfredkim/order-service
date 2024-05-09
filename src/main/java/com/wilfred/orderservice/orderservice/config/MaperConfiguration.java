package com.wilfred.orderservice.orderservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MaperConfiguration {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
