package com.restaurant.kitchenservice.service;

import com.restaurant.kitchenservice.dto.KitchenOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KitchenOrderService {

    KitchenOrderDto createKitchenOrder(KitchenOrderDto kitchenOrderDto);

    Page<KitchenOrderDto> getAllKitchenOrders(Pageable pageable);

    KitchenOrderDto updateKitchenStatus(Long id, String status);

    void deleteKitchenOrder(Long id);

    Page<KitchenOrderDto> getAllKitchenOrders(Pageable pageable, String status);
}
