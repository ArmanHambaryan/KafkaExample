package com.restaurant.kitchenservice.dto;

import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        Integer tableNumber,
        String items,
        String status,
        LocalDateTime receivedAt
) {
}

