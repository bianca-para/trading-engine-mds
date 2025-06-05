package org.dev.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;
import org.dev.server.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        OrderResponseDto canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable Long orderId) {
        OrderResponseDto orderDetails = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersForUser(@PathVariable UUID userId) {
        List<OrderResponseDto> orders = orderService.getAllOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }
}
