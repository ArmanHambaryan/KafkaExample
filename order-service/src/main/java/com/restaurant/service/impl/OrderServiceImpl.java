package com.restaurant.service.impl;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.repository.OrderRepository;
import com.restaurant.service.FileStorageService;
import com.restaurant.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_TOPIC = "order-topic";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final FileStorageService fileStorageService;
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper,
                            FileStorageService fileStorageService, KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.fileStorageService = fileStorageService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto, MultipartFile image) {
        Order order = orderMapper.toEntity(orderDto);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.store(image);
            order.setImageUrl(imageUrl);
        } else if (orderDto.imageUrl() != null) {
            order.setImageUrl(orderDto.imageUrl());
        }

        order.setStatus("PENDING");
        order.setReceivedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        OrderDto savedDto = orderMapper.toDto(savedOrder);

        kafkaTemplate.send(ORDER_TOPIC, savedDto);

        return savedDto;
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