package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.service.CouponService;
import kr.co.shoply.service.SiteInfoService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final SiteInfoService siteInfoService;

    @GetMapping("/admin/coupon/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, Principal principal) {
        PageResponseDTO<SysCouponDTO> pageResponse = couponService.selectCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        // 로그인한 사용자 이름(또는 발급처명) 조회 및 전달
        if (principal != null) {
            String loginId = principal.getName();

            int memLevel = couponService.findMemberLevelById(loginId);
            String issuerName;

            if (memLevel == 7) {
                // 일반관리자 또는 최고관리자
                issuerName = couponService.findMemberNameById(loginId);
            } else if (memLevel == 2) {
                // 판매자 (MEM_SELLER 테이블의 corp_name)
                issuerName = couponService.findSellerCorpNameById(loginId);
            } else {
                issuerName = "알 수 없는 사용자";
            }

            model.addAttribute("issuerName", issuerName);
            model.addAttribute("memLevel", memLevel);
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


    @ResponseBody
    @PostMapping("/admin/coupon/register")
    public String registerCoupon(@ModelAttribute SysCouponDTO couponDTO,
                                 Principal principal) {
        log.info("✅ [Controller] 쿠폰 등록 요청 수신: {}", couponDTO);

        try {
            String loginId = principal.getName();
            couponDTO.setCp_issuer_id(loginId);

            log.info("✅ [Controller] 발급자 ID: {}", loginId);
            log.info("✅ [Controller] 쿠폰 정보 - 이름: {}, 타입: {}, 금액: {}",
                    couponDTO.getCp_name(), couponDTO.getCp_type(), couponDTO.getCp_value());

            int memLevel = couponService.findMemberLevelById(loginId);
            log.info("✅ [Controller] 회원등급: {}", memLevel);

            int type = couponDTO.getCp_type();

            if (memLevel == 2 && type != 1) {
                log.warn("🚫 일반판매자가 잘못된 쿠폰 등록 시도");
                return "denied";
            } else if (memLevel == 7 && (type != 2 && type != 3)) {
                log.warn("🚫 최고관리자가 잘못된 쿠폰 등록 시도");
                return "denied";
            }

            couponService.registerCoupon(couponDTO);
            log.info("✅ [Controller] CouponService.registerCoupon() 호출 완료");

            return "success";

        } catch (Exception e) {
            log.error("❌ [Controller] 쿠폰 등록 실패", e);
            return "fail";
        }
    }





    @GetMapping("/admin/coupon/issued")
    public String issuedCouponList(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<UserCouponDTO> pageResponse = couponService.selectUserCouponList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);


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
