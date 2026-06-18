    package com.restaurant.kitchenservice.service.impl;

    import com.restaurant.kitchenservice.dto.KitchenOrderDto;
    import com.restaurant.kitchenservice.dto.OrderStatusEvent;
    import com.restaurant.kitchenservice.entity.KitchenOrder;
    import com.restaurant.kitchenservice.mapper.KitchenOrderMapper;
    import com.restaurant.kitchenservice.repository.KitchenOrderRepository;
    import com.restaurant.kitchenservice.service.KitchenOrderService;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.kafka.core.KafkaTemplate;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    public class KitchenOrderServiceImpl implements KitchenOrderService {

        private final KitchenOrderRepository kitchenOrderRepository;
        private final KitchenOrderMapper mapper;
        private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

        public KitchenOrderServiceImpl(KitchenOrderRepository kitchenOrderRepository,
                                       KitchenOrderMapper mapper,
                                       @Qualifier("kafkaTemplate") KafkaTemplate<String, OrderStatusEvent> kafkaTemplate) {
            this.kitchenOrderRepository = kitchenOrderRepository;
            this.mapper = mapper;
            this.kafkaTemplate = kafkaTemplate;}

        @Transactional
        public synchronized KitchenOrderDto createKitchenOrder(KitchenOrderDto dto) {
            if (kitchenOrderRepository.existsByOrderId(dto.orderId())) {
                System.out.println("Order already exists, skipping: " + dto.orderId());
                return null;
            }

            KitchenOrder entity = mapper.toEntity(dto);
            return mapper.toDto(kitchenOrderRepository.save(entity));
        }

        @Override
        @Transactional
        public KitchenOrderDto updateKitchenStatus(Long id, String status) {
            KitchenOrder order = kitchenOrderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Kitchen order not found"));

            order.setKitchenStatus(status);
            KitchenOrder updated = kitchenOrderRepository.save(order);

            OrderStatusEvent event = new OrderStatusEvent(updated.getId(), status);
            kafkaTemplate.send("order-status-topic", String.valueOf(updated.getId()), event);

            return mapper.toDto(updated);
        }

        @Override
        public Page<KitchenOrderDto> getAllKitchenOrders(Pageable pageable, String status) {
            Page<KitchenOrder> kitchenOrders = (status != null && !status.isBlank())
                    ? kitchenOrderRepository.findByKitchenStatus(status, pageable)
                    : kitchenOrderRepository.findAll(pageable);
            return kitchenOrders.map(mapper::toDto);
        }

        @Override
        @Transactional
        public void deleteKitchenOrder(Long id) {
            kitchenOrderRepository.deleteById(id);
        }
    }
