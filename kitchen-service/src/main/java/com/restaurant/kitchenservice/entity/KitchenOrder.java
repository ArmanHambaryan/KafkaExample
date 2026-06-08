package com.restaurant.kitchenservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kitchen_orders")
@Builder
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "kitchen_status", nullable = false)
    private String kitchenStatus;

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
}