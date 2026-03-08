package com.zippyroute.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    @JsonIgnore
    private Order order;

    @Column(nullable = false)
    private String currentStatus = "PENDING"; // PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String deliveryPersonName;

    @Column
    private String deliveryPersonPhone;

    @Column
    private String estimatedDeliveryTime; // in minutes

    @Column(columnDefinition = "TEXT")
    private String statusHistory; // JSON format of status updates

    @Column(nullable = false)
    private Long createdAt = System.currentTimeMillis();

    @Column
    private Long updatedAt = System.currentTimeMillis();
}
