package kr.co.shoply.controller;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.SysCouponDTO;
import kr.co.shoply.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CouponController {

    private final CouponService couponService;

    @GetMapping("admin/coupon/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<SysCouponDTO> pageResponse = couponService.selectCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);
        return "admin/coupon/list";
    }


    // list에서 쿠폰 종료 확인하는거
    @ResponseBody
    @PostMapping("/admin/coupon/end")
    public String endCoupon(@RequestParam("cp_code") String cpCode) {
        couponService.endCoupon(cpCode); // 서비스에서 DB update
        return "success";
    }


    // 쿠폰 등록 처리
    @ResponseBody
    @PostMapping("/admin/coupon/register")
    public String registerCoupon(@ModelAttribute SysCouponDTO couponDTO) {
        log.info("쿠폰 등록 요청: {}", couponDTO);

        try {
            couponService.registerCoupon(couponDTO);
            return "success";
        } catch (Exception e) {
            log.error("쿠폰 등록 실패", e);
            return "fail";
        }
    }








    @GetMapping("admin/coupon/issued")
    public String issued() {
        return "admin/coupon/issued";
    }





}
