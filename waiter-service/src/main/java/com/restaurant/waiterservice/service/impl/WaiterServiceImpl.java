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
        String url = "http://kitchen-service:8082/api/kitchen/orders?page="
                + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&status=READY";

        ResponseEntity<KitchenOrderPageResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        KitchenOrderPageResponse body = response.getBody();
        List<KitchenOrderDto> orders = body != null && body.content() != null
                ? body.content()
                : List.of();
        long totalElements = body != null ? body.totalElements() : 0;

        return new PageImpl<>(orders, pageable, totalElements);
    }

    private record KitchenOrderPageResponse(
            List<KitchenOrderDto> content,
            long totalElements,
            int totalPages,
            int number,
            int size
    ) {}
}