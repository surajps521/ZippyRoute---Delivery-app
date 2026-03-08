package com.zippyroute.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String status;
    private Double totalAmount;
    private Double deliveryFee;
    private Double discount;
    private String deliveryAddress;
    private String phoneNumber;
    private String notes;
    private List<OrderItemDTO> items;
    private OrderTrackingDTO tracking;
    private Long createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long menuItemId;
        private String menuItemName;
        private Integer quantity;
        private Double price;
        private String specialInstructions;
    }
}
