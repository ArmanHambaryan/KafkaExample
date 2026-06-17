package com.restaurant.kitchenservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KitchenOrderDto(
        @JsonProperty("id") Long id,
        @JsonProperty("orderId") Long orderId,
        @JsonProperty("dishName") String dishName,
        @JsonProperty("kitchenStatus") String kitchenStatus,
        @JsonProperty("imageUrl") String imageUrl
) {}
