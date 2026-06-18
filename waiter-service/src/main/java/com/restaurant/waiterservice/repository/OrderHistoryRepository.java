package com.restaurant.waiterservice.repository;

import com.restaurant.waiterservice.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}