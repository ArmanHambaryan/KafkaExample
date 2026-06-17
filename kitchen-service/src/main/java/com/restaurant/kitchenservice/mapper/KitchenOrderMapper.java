package com.restaurant.kitchenservice.mapper;

import com.restaurant.kitchenservice.dto.KitchenOrderDto;
import com.restaurant.kitchenservice.entity.KitchenOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KitchenOrderMapper {

    KitchenOrderDto toDto(KitchenOrder kitchenOrder);

    KitchenOrder toEntity(KitchenOrderDto kitchenOrderDto);
}