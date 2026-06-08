package com.restaurant.waiterservice.repository;

import com.restaurant.waiterservice.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
}