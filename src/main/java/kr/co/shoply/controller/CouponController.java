package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.service.CouponService;
import kr.co.shoply.service.VersionService;
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
    private final VersionService versionService;

    @GetMapping("/admin/coupon/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, Principal principal) {
        PageResponseDTO<SysCouponDTO> pageResponse = couponService.selectCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);

        // 로그인한 사용자 이름 조회 및 전달
        if (principal != null) {
            String loginId = principal.getName();
            String memName = couponService.findMemberNameById(loginId);
            model.addAttribute("loginName", memName);
        }

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/coupon/list";
    }



    // list에서 쿠폰 종료 확인하는거
    @ResponseBody
    @PostMapping("/admin/coupon/end")
    public String endCoupon(@RequestParam("cp_code") String cpCode) {

        // SYS_COUPON 상태 1 → 0 (발급중 → 종료)
        couponService.endCoupon(cpCode);

        // USER_COUPON 상태 1 → 3 (사용가능 → 발급종료)
        couponService.updateUserCouponStatus(cpCode, 3);
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



    @GetMapping("/admin/coupon/issued")
    public String issuedCouponList(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<UserCouponDTO> pageResponse = couponService.selectUserCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);


        // ✅ 모달에서 참조할 빈 객체 추가 (null 방지용)
        model.addAttribute("couponDetail", new UserCouponDTO());

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/coupon/issued";
    }





    @GetMapping("/admin/coupon/detail")
    @ResponseBody
    public UserCouponDTO getCouponDetail(@RequestParam("cpNo") String cpNo) {
        return couponService.selectUserCouponDetail(cpNo);
    }

    @GetMapping("/admin/coupon/user/detail")
    @ResponseBody
    public UserCouponDTO selectUserCouponDetail(@RequestParam("cp_no") String cpNo) {
        return couponService.selectUserCouponDetail(cpNo);
    }




    @PostMapping("/admin/coupon/updateUserCouponStatus")
    @ResponseBody
    public String updateUserCouponStatus(@RequestParam("cp_no") String cpNo,
                                         @RequestParam("cp_stat") int cpStat) {

        int nextStat = (cpStat == 1) ? 2 : 1;
        couponService.updateUserCouponStatus(cpNo, nextStat);
        return "success";
    }





}
