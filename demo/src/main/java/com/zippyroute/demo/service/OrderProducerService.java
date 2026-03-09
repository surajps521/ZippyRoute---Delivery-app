package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProducerService.class);
    private static final String TOPIC = "order-events";
    
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    public void publishOrderEvent(OrderEvent event) {
        try {
            logger.info("Publishing order event: {}", event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event);
        } catch (Exception e) {
            logger.error("Error publishing order event: {}", e.getMessage(), e);
        }
    }
}
