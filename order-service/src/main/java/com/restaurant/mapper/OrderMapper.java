package com.restaurant.mapper;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "imageUrl", target = "imageUrl")
    OrderDto toDto(Order order);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "imageUrl", target = "imageUrl")
    Order toEntity(OrderDto orderDto);

}
