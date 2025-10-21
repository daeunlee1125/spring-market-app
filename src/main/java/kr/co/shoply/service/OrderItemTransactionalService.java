package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemTransactionalService {

    private final OrderItemMapper orderItemMapper;

    @Transactional
    public void updateOrderStatusTransactional() {
        orderItemMapper.updateOrderItem();
    }
}