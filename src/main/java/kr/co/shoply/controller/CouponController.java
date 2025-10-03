package kr.co.shoply.controller;


import kr.co.shoply.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
public class CouponController {


    private final CouponService couponService;


    @GetMapping("admin/coupon/list")
    public String list(){

        return "admin/coupon/list";
    }

    @GetMapping("admin/coupon/issued")
    public String issued(){

        return "admin/coupon/issued";
    }

}
