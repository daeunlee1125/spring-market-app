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

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CouponController {

    private final CouponService couponService;

    @GetMapping("admin/coupon/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, Principal principal) {
        PageResponseDTO<SysCouponDTO> pageResponse = couponService.selectCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);

        // 로그인한 사용자 이름 조회 및 전달
        if (principal != null) {
            String loginId = principal.getName();
            String memName = couponService.findMemberNameById(loginId);
            model.addAttribute("loginName", memName);
        }

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
    public String registerCoupon(@ModelAttribute SysCouponDTO couponDTO,
                                 Principal principal) { // 로그인 유저 정보 가져오기
        log.info("쿠폰 등록 요청: {}", couponDTO);

        try {
            // 로그인한 사용자의 ID 저장
            String loginId = principal.getName();
            couponDTO.setCp_issuer_id(loginId);

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
