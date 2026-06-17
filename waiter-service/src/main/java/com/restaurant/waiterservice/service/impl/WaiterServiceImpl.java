package com.restaurant.waiterservice.service.impl;

import com.restaurant.waiterservice.kitchenservice.KitchenOrderDto;
import com.restaurant.waiterservice.entity.Waiter;
import com.restaurant.waiterservice.repository.WaiterRepository;
import com.restaurant.waiterservice.service.WaiterService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;
    private final RestTemplate restTemplate;

    public WaiterServiceImpl(WaiterRepository waiterRepository, RestTemplate restTemplate) {
        this.waiterRepository = waiterRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Waiter getWaiterById(Long id) {
        return waiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Waiter not found with id: " + id));
    }

    @Override
    public Page<KitchenOrderDto> getKitchenOrders(Pageable pageable) {
        String kitchenUrl = "http://kitchen-service:8082/api/kitchen/orders?page="
                + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&status=READY";

        ResponseEntity<KitchenOrderPageResponse> response = restTemplate.exchange(
                kitchenUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        KitchenOrderPageResponse body = response.getBody();
        List<KitchenOrderDto> orders = body != null && body.content() != null
                ? body.content()
                : List.of();

        List<KitchenOrderDto> ordersWithImages = orders.stream().map(order -> {
            if (order.imageUrl() != null && !order.imageUrl().isBlank()) {
                return order;
            }
            try {
                String orderUrl = "http://order-service:8081/api/orders/" + order.orderId();
                ResponseEntity<OrderDto> orderResponse = restTemplate.exchange(
                        orderUrl,
                        HttpMethod.GET,
                        null,
                        OrderDto.class
                );
                String imageUrl = orderResponse.getBody() != null
                        ? orderResponse.getBody().imageUrl()
                        : null;
                return new KitchenOrderDto(
                        order.id(),
                        order.orderId(),
                        order.dishName(),
                        order.kitchenStatus(),
                        imageUrl
                );
            } catch (Exception e) {
                System.err.println("Could not fetch image for orderId: " + order.orderId() + " — " + e.getMessage());
                return order;
            }
        }).toList();

        long totalElements = body != null ? body.totalElements() : 0;
        return new PageImpl<>(ordersWithImages, pageable, totalElements);
    }

    private record KitchenOrderPageResponse(
            List<KitchenOrderDto> content,
            long totalElements,
            int totalPages,
            int number,
            int size
    ) {}

    private record OrderDto(
            Long id,
            Integer tableNumber,
            String items,
            String status,
            String imageUrl
    ) {}
}