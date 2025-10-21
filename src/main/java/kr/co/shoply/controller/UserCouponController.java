package kr.co.shoply.controller;

import kr.co.shoply.dto.SysCouponDTO;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
public class UserCouponController {

    private final CouponService couponService;


    @PostMapping("/claimAllBySeller")
    public String claimAllBySeller(@RequestParam("seller_id") String sellerId,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails) {

        log.info("✅ [쿠폰 전체 발급 요청 시작] sellerId={}, myUserDetails={}", sellerId, myUserDetails);

        if (myUserDetails == null) {
            log.warn("⚠️ 로그인 필요: 사용자 정보가 null임");
            return "login_required";
        }

        String memId = myUserDetails.getUsername();
        log.info("✅ 사용자 ID: {}", memId);

        try {
            List<SysCouponDTO> coupons = couponService.getSellerCoupons(sellerId);
            log.info("✅ 조회된 쿠폰 개수: {}", (coupons == null ? "null" : coupons.size()));

            if (coupons == null || coupons.isEmpty()) {
                return "no_coupons";
            }

            for (SysCouponDTO coupon : coupons) {
                boolean already = couponService.hasUserCoupon(memId, coupon.getCp_code());
                log.info("쿠폰 [{}] 중복 여부: {}", coupon.getCp_code(), already);
                if (!already) {
                    couponService.issueUserCoupon(memId, coupon.getCp_code());
                    log.info("🎟️ 발급 성공: {}", coupon.getCp_code());
                }
            }

            log.info("✅ 전체 발급 완료");
            return "success";

        } catch (Exception e) {
            log.error("❌ 판매자 쿠폰 전체 발급 실패", e);
            return "error";
        }
    }

}

