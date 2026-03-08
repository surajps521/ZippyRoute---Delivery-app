package com.zippyroute.demo.controller;

import com.zippyroute.demo.dto.OrderTrackingDTO;
import com.zippyroute.demo.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracking")
@CrossOrigin(origins = "*")
public class OrderTrackingController {

    @Autowired
    private OrderTrackingService orderTrackingService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderTrackingDTO> getOrderTracking(@PathVariable Long orderId) {
        OrderTrackingDTO tracking = orderTrackingService.getOrderTracking(orderId);
        if (tracking != null) {
            return ResponseEntity.ok(tracking);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderTrackingDTO> updateOrderTracking(@PathVariable Long orderId, @RequestBody OrderTrackingDTO dto) {
        OrderTrackingDTO tracking = orderTrackingService.updateOrderTracking(orderId, dto);
        if (tracking != null) {
            return ResponseEntity.ok(tracking);
        }
        return ResponseEntity.notFound().build();
    }
}
