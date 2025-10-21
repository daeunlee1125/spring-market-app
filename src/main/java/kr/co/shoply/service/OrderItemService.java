package kr.co.shoply.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderItemService {


    private final OrderItemTransactionalService orderItemTransactionalService;

    @Scheduled(fixedRate = 15000)
    public void updateOrderStatus() {
        orderItemTransactionalService.updateOrderStatusTransactional();
    }

}
