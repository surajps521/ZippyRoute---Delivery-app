package com.zippyroute.demo.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long orderId;
    private Long userId;
    private String status;
    private Double totalAmount;
    private String deliveryAddress;
    private String phoneNumber;
    private String eventType; // CREATED, STATUS_UPDATED, CANCELLED
    private Long timestamp;
    private String notes;
}
