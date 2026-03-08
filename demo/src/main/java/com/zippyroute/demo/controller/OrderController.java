package com.zippyroute.demo.controller;

import com.zippyroute.demo.dto.CreateOrderRequest;
import com.zippyroute.demo.dto.ErrorResponse;
import com.zippyroute.demo.dto.OrderDTO;
import com.zippyroute.demo.service.OrderService;
import com.zippyroute.demo.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestHeader("Authorization") String authHeader, @RequestBody CreateOrderRequest request) {
        // Extract userId from token
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid or missing authentication token"));
        }

        try {
            OrderDTO order = orderService.createOrder(userId, request);
            if (order != null) {
                return ResponseEntity.ok(order);
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to create order"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Error creating order: " + e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        OrderDTO order = orderService.getOrderById(orderId, userId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/my-orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<OrderDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        OrderDTO order = orderService.cancelOrder(orderId, userId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.badRequest().build();
    }

    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtTokenProvider.getUserIdFromToken(token);
        }
        return null;
    }
}
