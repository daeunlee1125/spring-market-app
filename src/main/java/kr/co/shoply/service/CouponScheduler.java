package kr.co.shoply.service;

import kr.co.shoply.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final CouponService couponService;

    // 매일 자정(00:00)에 만료된 쿠폰 상태 업데이트

    @Scheduled(cron = "0 0 0 * * *")  // 매일 00:00 실행
    public void updateExpiredCoupons() {
        log.info(" [SCHEDULER] 자정 만료 쿠폰 상태 업데이트 시작...");
        couponService.expireUserCoupons();
        log.info("[SCHEDULER] 자정 만료 쿠폰 상태 업데이트 완료");
    }
}
