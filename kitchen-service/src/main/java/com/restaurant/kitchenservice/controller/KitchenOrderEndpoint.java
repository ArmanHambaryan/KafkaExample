package com.restaurant.kitchenservice.controller;

import com.restaurant.kitchenservice.dto.KitchenOrderDto;
import com.restaurant.kitchenservice.service.KitchenOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kitchen/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class KitchenOrderEndpoint {

    private final KitchenOrderService kitchenOrderService;

    public KitchenOrderEndpoint(KitchenOrderService kitchenOrderService) {
        this.kitchenOrderService = kitchenOrderService;
    }

    @PostMapping
    public ResponseEntity<KitchenOrderDto> createKitchenOrder(@RequestBody KitchenOrderDto dto) {
        KitchenOrderDto created = kitchenOrderService.createKitchenOrder(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<KitchenOrderDto>> getAllKitchenOrders(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(kitchenOrderService.getAllKitchenOrders(pageable));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<KitchenOrderDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(kitchenOrderService.updateKitchenStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKitchenOrder(@PathVariable Long id) {
        kitchenOrderService.deleteKitchenOrder(id);
        return ResponseEntity.noContent().build();
    }


}
