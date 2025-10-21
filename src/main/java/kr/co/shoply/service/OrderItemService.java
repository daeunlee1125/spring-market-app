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

    @Scheduled(fixedRate = 10000)
    public void updateOrderStatus() {
        System.out.println("[Scheduler] OrderItemService 실행됨");
        orderItemTransactionalService.updateOrderStatusTransactional();
    }

    @PostConstruct
    public void init() {
        System.out.println("[Scheduler] OrderStatusScheduler Bean Loaded!");
    }
}
