package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.OrderTrackingDTO;
import com.zippyroute.demo.entity.OrderTracking;
import com.zippyroute.demo.repository.OrderTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderTrackingRepository orderTrackingRepository;

    public OrderTrackingDTO getOrderTracking(Long orderId) {
        OrderTracking tracking = orderTrackingRepository.findByOrderId(orderId).orElse(null);
        return tracking != null ? convertToDTO(tracking) : null;
    }

    public OrderTrackingDTO updateOrderTracking(Long orderId, OrderTrackingDTO dto) {
        OrderTracking tracking = orderTrackingRepository.findByOrderId(orderId).orElse(null);
        if (tracking != null) {
            tracking.setCurrentStatus(dto.getCurrentStatus());
            tracking.setLatitude(dto.getLatitude());
            tracking.setLongitude(dto.getLongitude());
            tracking.setDeliveryPersonName(dto.getDeliveryPersonName());
            tracking.setDeliveryPersonPhone(dto.getDeliveryPersonPhone());
            tracking.setEstimatedDeliveryTime(dto.getEstimatedDeliveryTime());
            tracking.setUpdatedAt(System.currentTimeMillis());

            OrderTracking updated = orderTrackingRepository.save(tracking);
            return convertToDTO(updated);
        }
        return null;
    }

    private OrderTrackingDTO convertToDTO(OrderTracking tracking) {
        OrderTrackingDTO dto = new OrderTrackingDTO();
        dto.setId(tracking.getId());
        dto.setCurrentStatus(tracking.getCurrentStatus());
        dto.setLatitude(tracking.getLatitude());
        dto.setLongitude(tracking.getLongitude());
        dto.setDeliveryPersonName(tracking.getDeliveryPersonName());
        dto.setDeliveryPersonPhone(tracking.getDeliveryPersonPhone());
        dto.setEstimatedDeliveryTime(tracking.getEstimatedDeliveryTime());
        return dto;
    }
}
