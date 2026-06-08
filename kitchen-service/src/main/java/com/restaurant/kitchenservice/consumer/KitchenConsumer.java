package com.restaurant.kitchenservice.consumer;

import com.restaurant.kitchenservice.dto.OrderDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KitchenConsumer {

    @KafkaListener(topics = "order-topic", groupId = "kitchen-group")
    public void listen(OrderDto orderDto) {
        System.out.println("KitchenConsumer listen N= " + orderDto.tableNumber());
    }
}