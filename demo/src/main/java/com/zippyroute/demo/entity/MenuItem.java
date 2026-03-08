package com.zippyroute.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column
    private Double rating = 4.5;

    @Column
    private Integer reviewCount = 0;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(nullable = false)
    private Long createdAt = System.currentTimeMillis();

    @Column
    private Long updatedAt = System.currentTimeMillis();
}
