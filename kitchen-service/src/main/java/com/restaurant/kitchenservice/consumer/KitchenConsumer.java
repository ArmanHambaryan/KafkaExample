package com.restaurant.kitchenservice.consumer;

import com.restaurant.kitchenservice.dto.KitchenOrderDto;
import com.restaurant.kitchenservice.dto.OrderDto;
import com.restaurant.kitchenservice.service.KitchenOrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KitchenConsumer {

    private final KitchenOrderService kitchenOrderService;

    public KitchenConsumer(KitchenOrderService kitchenOrderService) {
        this.kitchenOrderService = kitchenOrderService;
    }

    @KafkaListener(topics = "order-topic", groupId = "kitchen-group")
    public void listen(OrderDto orderDto) {
        try {
            System.out.println("KitchenConsumer listen N= " + orderDto.tableNumber() + " with OrderID= " + orderDto.id());

            KitchenOrderDto kitchenOrderDto = new KitchenOrderDto(
                    null,
                    orderDto.id(),
                    orderDto.items(),
                    "RECEIVED",
                    orderDto.imageUrl()
            );

            kitchenOrderService.createKitchenOrder(kitchenOrderDto);
        } catch (Exception e) {
            System.err.println("KitchenConsumer error: " + e.getMessage());
        }
    }
}