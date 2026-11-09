package com.orderprocessor.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

@Tag(name = "Orders", description = "Endpoints for order management and processing")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Operation (summary = "Check order API availability", description = "Checks whether the order API is running and responding to requests.")
    @ApiResponse(responseCode = "200", description = "Order API is running successfully")
    @GetMapping
    public String test() {
        return "Order API is working!";
    }

    @Operation(summary = "Create a new order", description = "Receives an order, validates its information, processes it through the order processing chain and persists the result.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created and processed"),
            @ApiResponse(responseCode = "422", description = "Order rejected due to business validation rules"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrderRequest(@RequestBody @Valid CreateOrderRequest orderRequest) {
        var orderResponse = orderService.receberPedido(orderRequest);
        if (orderResponse.getStatus() == OrderStatus.REJECTED)
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(orderResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

}
