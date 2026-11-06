package com.orderprocessor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.application.dto.OrderResponse;
import com.orderprocessor.application.service.OrderService;
import com.orderprocessor.domain.model.OrderStatus;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public String test() {
        return "Order API is working!";
    }

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrderRequest(@RequestBody @Valid CreateOrderRequest orderRequest) {
        var orderResponse = orderService.receberPedido(orderRequest);
        if(orderResponse.getStatus() == OrderStatus.REJECTED)
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(orderResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
    
}
