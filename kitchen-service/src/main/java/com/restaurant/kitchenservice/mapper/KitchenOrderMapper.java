package com.restaurant.kitchenservice.mapper;

import com.restaurant.kitchenservice.dto.KitchenOrderDto;
import com.restaurant.kitchenservice.entity.KitchenOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KitchenOrderMapper {

    @Mapping(source = "imageUrl", target = "imageUrl")
    KitchenOrderDto toDto(KitchenOrder kitchenOrder);

    @Mapping(source = "imageUrl", target = "imageUrl")
    KitchenOrder toEntity(KitchenOrderDto kitchenOrderDto);
}