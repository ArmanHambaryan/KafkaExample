package com.restaurant.waiterservice.repository;

import com.restaurant.waiterservice.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}