package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumerService.class);
    
    @KafkaListener(topics = "order-events", groupId = "order-service-group")
    public void consumeOrderEvent(OrderEvent event) {
        try {
            logger.info("Received order event: Order ID: {}, Event Type: {}, Status: {}", 
                event.getOrderId(), event.getEventType(), event.getStatus());
            
            // Handle different event types
            switch (event.getEventType()) {
                case "CREATED":
                    handleOrderCreated(event);
                    break;
                case "STATUS_UPDATED":
                    handleOrderStatusUpdated(event);
                    break;
                case "CANCELLED":
                    handleOrderCancelled(event);
                    break;
                default:
                    logger.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error consuming order event: {}", e.getMessage(), e);
        }
    }
    
    private void handleOrderCreated(OrderEvent event) {
        logger.info("Order created: {} for user {} at {}", 
            event.getOrderId(), event.getUserId(), event.getDeliveryAddress());
        // TODO: Send notification, log to analytics, etc.
    }
    
    private void handleOrderStatusUpdated(OrderEvent event) {
        logger.info("Order {} status updated to: {}", event.getOrderId(), event.getStatus());
        // TODO: Send status notification to customer, update tracking, etc.
    }
    
    private void handleOrderCancelled(OrderEvent event) {
        logger.info("Order {} cancelled", event.getOrderId());
        // TODO: Process refund, send cancellation notification, etc.
    }
}
