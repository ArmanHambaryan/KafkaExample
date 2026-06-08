package com.restaurant.waiterservice.consumer;

import com.restaurant.waiterservice.entity.OrderHistory;
import com.restaurant.waiterservice.kitchenservice.dto.OrderStatusEvent;
import com.restaurant.waiterservice.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderStatusConsumer {

    private final OrderHistoryRepository repository;

    public OrderStatusConsumer(OrderHistoryRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "order-status-topic", groupId = "waiter-group")
    public void listen(OrderStatusEvent event) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(event.orderId());
        history.setStatus(event.status());
        history.setCreatedAt(LocalDateTime.now());
        repository.save(history);
        System.out.println("Ծանուցում ստացվեց. Պատվեր №" + event.orderId() +
                " այժմ գտնվում է՝ " + event.status() + " կարգավիճակում։");
    }
}