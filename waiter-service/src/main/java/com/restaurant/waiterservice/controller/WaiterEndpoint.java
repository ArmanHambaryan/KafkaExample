package com.restaurant.waiterservice.controller;

import com.restaurant.waiterservice.kitchenservice.KitchenOrderDto;
import com.restaurant.waiterservice.service.WaiterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/waiter")
public class WaiterEndpoint {

    private final WaiterService waiterService;

    public WaiterEndpoint(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<KitchenOrderDto>> getOrders(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(waiterService.getKitchenOrders(pageable));
    }
}