package com.restaurant.kitchenservice.dto;

public record OrderStatusEvent(Long orderId, String status) {}