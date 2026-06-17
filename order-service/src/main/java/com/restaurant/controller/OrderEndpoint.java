package com.restaurant.controller;

import com.restaurant.dto.OrderDto;
import com.restaurant.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orders")
public class OrderEndpoint {

    private final OrderService orderService;

    public OrderEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<OrderDto> createOrder(
            @RequestPart("order") OrderDto orderDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        OrderDto created = orderService.createOrder(orderDto, image);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<OrderDto> dtoPage = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
