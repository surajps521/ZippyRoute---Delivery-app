package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.CreateOrderRequest;
import com.zippyroute.demo.dto.OrderDTO;
import com.zippyroute.demo.dto.OrderEvent;
import com.zippyroute.demo.entity.*;
import com.zippyroute.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderTrackingRepository orderTrackingRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderProducerService orderProducerService;

    public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setNotes(request.getNotes());
        order.setStatus("PENDING");

        Double totalAmount = 0.0;
        Set<OrderItem> items = new HashSet<>();

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId()).orElse(null);
            if (menuItem != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(menuItem.getPrice());
                orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

                items.add(orderItem);
                totalAmount += menuItem.getPrice() * itemRequest.getQuantity();
            }
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Create order tracking
        OrderTracking tracking = new OrderTracking();
        tracking.setOrder(savedOrder);
        tracking.setCurrentStatus("PENDING");
        tracking.setEstimatedDeliveryTime("30");
        orderTrackingRepository.save(tracking);

        // Publish order created event to Kafka
        publishOrderEvent(savedOrder, "CREATED");

        return convertToDTO(savedOrder);
    }

    public OrderDTO getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElse(null);
        return order != null ? convertToDTO(order) : null;
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(newStatus);
            order.setUpdatedAt(System.currentTimeMillis());
            Order updated = orderRepository.save(order);

            // Update tracking
            OrderTracking tracking = orderTrackingRepository.findByOrderId(orderId).orElse(null);
            if (tracking != null) {
                tracking.setCurrentStatus(newStatus);
                tracking.setUpdatedAt(System.currentTimeMillis());
                orderTrackingRepository.save(tracking);
            }

            // Publish order status updated event to Kafka
            publishOrderEvent(updated, "STATUS_UPDATED");

            return convertToDTO(updated);
        }
        return null;
    }

    public OrderDTO cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElse(null);
        if (order != null && (order.getStatus().equals("PENDING") || order.getStatus().equals("CONFIRMED"))) {
            order.setStatus("CANCELLED");
            order.setUpdatedAt(System.currentTimeMillis());
            Order updated = orderRepository.save(order);

            // Update tracking
            OrderTracking tracking = orderTrackingRepository.findByOrderId(orderId).orElse(null);
            if (tracking != null) {
                tracking.setCurrentStatus("CANCELLED");
                tracking.setUpdatedAt(System.currentTimeMillis());
                orderTrackingRepository.save(tracking);
            }

            // Publish order cancelled event to Kafka
            publishOrderEvent(updated, "CANCELLED");

            return convertToDTO(updated);
        }
        return null;
    }

    private void publishOrderEvent(Order order, String eventType) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUser().getId());
        event.setStatus(order.getStatus());
        event.setTotalAmount(order.getTotalAmount());
        event.setDeliveryAddress(order.getDeliveryAddress());
        event.setPhoneNumber(order.getPhoneNumber());
        event.setEventType(eventType);
        event.setTimestamp(System.currentTimeMillis());
        event.setNotes(order.getNotes());

        orderProducerService.publishOrderEvent(event);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setDiscount(order.getDiscount());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getItems() != null) {
            List<OrderDTO.OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(item -> new OrderDTO.OrderItemDTO(
                            item.getMenuItem().getId(),
                            item.getMenuItem().getName(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getSpecialInstructions()
                    ))
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }
}
