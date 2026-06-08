package com.restaurant.waiterservice.kitchenservice;

public record KitchenOrderDto(
        Long id,
        Long orderId,
        String dishName,
        String kitchenStatus
) {}