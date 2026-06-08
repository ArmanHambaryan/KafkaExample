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
                + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        ResponseEntity<List<KitchenOrderDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<KitchenOrderDto> orders = response.getBody();
        return new PageImpl<>(orders != null ? orders : List.of(), pageable, orders != null ? orders.size() : 0);
    }

}