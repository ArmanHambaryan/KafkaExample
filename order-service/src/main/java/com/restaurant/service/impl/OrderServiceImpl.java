package com.restaurant.service.impl;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.repository.OrderRepository;
import com.restaurant.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {


    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(KafkaTemplate<String, OrderDto> kafkaTemplate, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        if (orderDto.imageUrl() != null) {
            order.setImageUrl(orderDto.imageUrl());
        }
        order.setStatus("PENDING");
        order.setReceivedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
