package com.zippyroute.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingDTO {
    private Long id;
    private String currentStatus;
    private Double latitude;
    private Double longitude;
    private String deliveryPersonName;
    private String deliveryPersonPhone;
    private String estimatedDeliveryTime;
}
