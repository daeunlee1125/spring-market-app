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

import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ProdOptionDTO;
import kr.co.shoply.dto.ProFileDTO;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j

@RequiredArgsConstructor

@Controller

@RequestMapping("/my")

public class MyController {



    private final MyService myService;



    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String mem_id = user.getMember().getMem_id();

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

        // productMap 추가
        Map<String, ProductDTO> productMap = homeData.getRecentOrders().stream()
                .map(OrderItemDTO::getProd_no)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(
                        prodNo -> prodNo,
                        prodNo -> myService.getProduct3(prodNo)
                ));
        model.addAttribute("productMap", productMap);

        return "my/home";
    }


    @GetMapping("/info")
    public String info(Model model, @AuthenticationPrincipal MyUserDetails user) {
        // 세션 대신 Spring Security 인증 정보 사용
        String mem_id = user.getMember().getMem_id();
        MemberDTO memberInfo = myService.getMemberInfo(mem_id);
        model.addAttribute("memberInfo", memberInfo);
        // 세션 DTO는 더 이상 필요하지 않습니다.
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

        // productMap 추가
        Map<String, ProductDTO> productMap = homeData.getRecentOrders().stream()
                .map(OrderItemDTO::getProd_no)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(
                        prodNo -> prodNo,
                        prodNo -> myService.getProduct3(prodNo)
                ));
        model.addAttribute("productMap", productMap);

        return "my/info";
    }

    @PostMapping("/info/update")
    @ResponseBody
    public ResponseEntity<String> updateInfo(@RequestBody MemberDTO memberDTO, @AuthenticationPrincipal MyUserDetails user) {
        String mem_id = user.getMember().getMem_id();
        memberDTO.setMem_id(mem_id);
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
    public ResponseEntity<String> confirmOrder(@RequestParam("item_no") Long itemNo,
                                               @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        myService.confirmOrder(itemNo, user.getMember().getMem_id());
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
    public String coupon(Model model,
                         @AuthenticationPrincipal MyUserDetails user) {
        log.info("coupon() GET 요청...");

        String mem_id = user.getMember().getMem_id(); // MyUserDetails에서 mem_id 가져오기

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

        // 상품 정보 Map 추가
        Map<String, ProductDTO> productMap = new HashMap<>();
        for (OrderItemDTO item : orderList) {
            ProductDTO product = myService.getProduct3(item.getProd_no());
            if (product != null) {
                productMap.put(item.getProd_no(), product);
            }
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("productMap", productMap);

        // 상단 요약 정보
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/order";
    }

    @GetMapping("/view/{prodNo}")
    public String viewProduct(@PathVariable String prodNo, Model model) {
        log.info("viewProduct() GET 요청... prodNo={}", prodNo);

        // 1. 상품 기본 정보
        ProductDTO product = myService.getProduct3(prodNo);

        if (product != null) {
            // 2. 옵션 리스트
            List<ProdOptionDTO> options = product.getOptions();
            if (options == null) {
                options = myService.getProductOption3(prodNo);
                product.setOptions(options);
            }

            // 3. 파일 리스트
            List<ProFileDTO> files = product.getFiles();
            if (files == null) {
                files = myService.getProductFiles(prodNo);
                product.setFiles(files);
            }
        } else {
            log.warn("해당 상품(prodNo={})이 존재하지 않습니다.", prodNo);
        }

        // 4. 모델에 담기
        model.addAttribute("product", product);

        return "my/product/view"; // templates/my/product/view.html
    }

    // MyController.java
    @GetMapping("/point")
    public String point(Model model, @AuthenticationPrincipal MyUserDetails user,
                         @RequestParam(defaultValue = "1") int pg) {
        String mem_id = user.getMember().getMem_id();

        // DTO를 수정하지 않는 경우, 서비스에서 String으로 변환
        List<PointDTO> pointHistory = myService.getPointHistory(mem_id);
        model.addAttribute("pointHistory", pointHistory);

        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());

        return "my/point";
    }

    @Controller
    @RequestMapping("/my")
    public class MyPageController {

        private final MyService myService;

        public MyPageController(MyService myService) {
            this.myService = myService;
        }

        @GetMapping("/qna")
        public String qnaPage(Model model, @AuthenticationPrincipal MyUserDetails user) {
            String mem_id = user.getMember().getMem_id();

            // 최근 문의 내역
            List<QnaDTO> recentQnas = myService.getRecentQnas(mem_id);
            model.addAttribute("recentQnas", recentQnas);

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

            return "my/qna";
        }

    }



}

