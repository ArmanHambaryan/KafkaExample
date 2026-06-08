package com.restaurant.service;

import com.restaurant.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface  OrderService {

    OrderDto createOrder(OrderDto orderDto);
    Page<OrderDto> getAllOrders(Pageable pageable);
    OrderDto getOrderById(Long id);

}
