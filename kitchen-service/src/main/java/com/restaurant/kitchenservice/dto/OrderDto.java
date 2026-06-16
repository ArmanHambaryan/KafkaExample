package com.restaurant.kitchenservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        Integer tableNumber,
        String items,
        String status,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime receivedAt,
        String imageUrl
) {
}

