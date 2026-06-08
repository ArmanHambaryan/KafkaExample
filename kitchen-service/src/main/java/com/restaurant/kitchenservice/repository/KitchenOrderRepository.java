package com.restaurant.kitchenservice.repository;

import com.restaurant.kitchenservice.entity.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
}
