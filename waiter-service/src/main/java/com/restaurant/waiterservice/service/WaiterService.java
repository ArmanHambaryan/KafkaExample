package com.restaurant.waiterservice.service;

import com.restaurant.waiterservice.kitchenservice.KitchenOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaiterService {
    Page<KitchenOrderDto> getKitchenOrders(Pageable pageable);}