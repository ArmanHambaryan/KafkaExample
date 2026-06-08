package com.restaurant.waiterservice.kitchenservice.dto;

public record OrderStatusEvent(Long orderId, String status) {}