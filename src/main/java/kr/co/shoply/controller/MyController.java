package kr.co.shoply.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.shoply.dto.*;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;


@Slf4j

@RequiredArgsConstructor

@Controller

@RequestMapping("/my")

public class MyController {



    private final MyService myService;



    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal MyUserDetails user) {
        log.info("home() GET 요청...");

        String mem_id = user.getMember().getMem_id();
        log.info("로그인한 사용자 ID: {}", mem_id);

        // MyService에서 한 번에 DTO 가져오기
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);

        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());
        model.addAttribute("recentOrders", homeData.getRecentOrders());
        model.addAttribute("recentPoints", homeData.getRecentPoints());
        model.addAttribute("recentReviews", homeData.getRecentReviews());
        model.addAttribute("recentQnas", homeData.getRecentQnas());

        return "my/home";
    }


    @GetMapping("/info")

    public String info(Model model, HttpSession session) {

        log.info("info() GET 요청...");



        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");



        if (sessUser == null) {

            return "redirect:/member/login";

        }



        String mem_id = sessUser.getMem_id();

        MemberDTO memberInfo = myService.getMemberInfo(mem_id);



        model.addAttribute("memberInfo", memberInfo);

        model.addAttribute("sessUser", sessUser);



        return "my/info";

    }



    @PostMapping("/info/update")

    @ResponseBody

    public ResponseEntity<String> updateInfo(@RequestBody MemberDTO memberDTO, HttpSession session) {

        log.info("updateInfo() POST 요청...");



        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

        }



        memberDTO.setMem_id(sessUser.getMem_id());

        myService.updateMemberInfo(memberDTO);



        return ResponseEntity.ok("success");

    }



    @PostMapping("/info/changePassword")

    @ResponseBody

    public ResponseEntity<String> changePassword(@RequestBody String newPassword, HttpSession session) {

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

        }



        myService.changePassword(sessUser.getMem_id(), newPassword);

        return ResponseEntity.ok("success");

    }



    @PostMapping("/info/withdrawal")

    public String withdrawal(HttpSession session) {

        log.info("withdrawal() POST 요청...");



        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return "redirect:/member/login";

        }



        myService.withdrawMember(sessUser.getMem_id());

        session.invalidate();



        return "redirect:/member/login?withdrawal=true";

    }



    @PostMapping("/order/confirm")

    @ResponseBody

    public ResponseEntity<String> confirmOrder(@RequestParam("item_no") Long itemNo, HttpSession session) {

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

        }



        myService.confirmOrder(itemNo, sessUser.getMem_id());

        return ResponseEntity.ok("success");

    }



    @PostMapping("/review/write")

    @ResponseBody

    public ResponseEntity<String> writeReview(@ModelAttribute ReviewDTO reviewDTO, HttpSession session) {

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

        }



        reviewDTO.setMem_id(sessUser.getMem_id());

        myService.writeReview(reviewDTO);

        return ResponseEntity.ok("success");

    }



    @PostMapping("/qna/write")

    @ResponseBody

    public ResponseEntity<String> writeQna(@ModelAttribute QnaDTO qnaDTO, HttpSession session) {

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

        }



        qnaDTO.setMem_id(sessUser.getMem_id());

        myService.writeQna(qnaDTO);

        return ResponseEntity.ok("success");

    }

    @GetMapping("/review")
    public String review(Model model, HttpSession session, @AuthenticationPrincipal MyUserDetails user) {
        log.info("review() GET 요청...");

        // 세션 대신 Security 인증 정보 사용
        String mem_id = user.getMember().getMem_id();

        // 서비스에서 해당 회원의 리뷰 리스트 가져오기
        List<ReviewDTO> reviewList = myService.getReviewsByMemId(mem_id);
        model.addAttribute("reviewList", reviewList);

        // 상단 요약 정보 가져오기
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/review"; // templates/my/review.html
    }

    @GetMapping("/coupon")
    public String coupon(Model model, HttpSession session) {
        log.info("coupon() GET 요청...");

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return "redirect:/member/login";
        }

        String mem_id = sessUser.getMem_id();

        // 사용자 쿠폰 리스트 가져오기
        List<UserCouponDTO> userCoupons = myService.getUserCouponsByMemId(mem_id);
        model.addAttribute("userCoupons", userCoupons);

        // 상단 요약 정보
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/coupon"; // templates/my/coupon.html
    }

    @GetMapping("/points")
    public String points(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String mem_id = user.getMember().getMem_id();

        List<PointDTO> pointHistory = myService.getPointHistory(mem_id);
        model.addAttribute("pointHistory", pointHistory);

        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/points"; // templates/my/points.html
    }

    @PostMapping("/order/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@RequestParam("item_no") Long itemNo, HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        myService.cancelOrder(itemNo, sessUser.getMem_id());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/info/withdrawal")
    public String withdrawalPage(HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return "redirect:/member/login";
        }
        return "my/withdrawal"; // templates/my/withdrawal.html
    }


    @GetMapping("/order")
    public String orderPage(Model model, @AuthenticationPrincipal MyUserDetails user) {
        log.info("orderPage() GET 요청...");

        String mem_id = user.getMember().getMem_id();

        // 주문 내역
        List<OrderItemDTO> orderList = myService.getOrdersByMemId(mem_id);
        model.addAttribute("orderList", orderList);

        // 상단 요약 정보
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/order"; // templates/my/order.html
    }

}

