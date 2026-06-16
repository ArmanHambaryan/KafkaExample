package com.restaurant.kitchenservice.repository;

import com.restaurant.kitchenservice.entity.KitchenOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {

    Page<KitchenOrder> findByKitchenStatus(String kitchenStatus, Pageable pageable);
    boolean existsByOrderId(Long orderId);
}
