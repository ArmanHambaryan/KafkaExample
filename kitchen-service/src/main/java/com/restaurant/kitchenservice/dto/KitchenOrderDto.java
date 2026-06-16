package com.restaurant.kitchenservice.dto;

public record KitchenOrderDto(
        Long id,
        Long orderId,
        String dishName,
        String kitchenStatus,
        String imageUrl

) {}