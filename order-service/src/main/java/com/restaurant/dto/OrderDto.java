package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record OrderDto(
        @JsonProperty("id") Long id,
        @JsonProperty("tableNumber") Integer tableNumber,
        @JsonProperty("items") String items,
        @JsonProperty("status") String status,
        @JsonProperty("receivedAt") LocalDateTime receivedAt,
        @JsonProperty("imageUrl") String imageUrl
) {
}
