package com.restaurant.waiterservice.service;

import com.restaurant.waiterservice.kitchenservice.KitchenOrderDto;
import com.restaurant.waiterservice.entity.Waiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaiterService {
    Waiter getWaiterById(Long id);
    Page<KitchenOrderDto> getKitchenOrders(Pageable pageable);}