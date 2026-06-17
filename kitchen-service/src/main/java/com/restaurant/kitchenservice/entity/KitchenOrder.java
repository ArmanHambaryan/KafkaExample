package com.restaurant.kitchenservice.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "kitchen_orders")
@Builder
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "kitchen_status", nullable = false)
    private String kitchenStatus;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl;

    public KitchenOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(String kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
