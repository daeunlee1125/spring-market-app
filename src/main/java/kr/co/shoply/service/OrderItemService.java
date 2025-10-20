package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    public OrderItemService(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @Transactional
    @Scheduled(fixedRate = 10000) // 10초마다 실행 (10000ms)
    public void updateOrderStatus() {
        orderItemMapper.updateOrderItem();
    }
}
