package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MyService myService;
    private final String uploadDir = "C:/shoply/uploads/";

    // ===================== 공통 메소드 =====================
    private void addMyPageSummary(Model model, String memberId) {
        MyPageHomeDTO homeData = myService.getMyPageHomeData(memberId);

        // 기본 정보 추가
        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());
        model.addAttribute("recentOrders", homeData.getRecentOrders());
        model.addAttribute("recentPoints", homeData.getRecentPoints());
        model.addAttribute("recentReviews", homeData.getRecentReviews());
        model.addAttribute("recentQnas", homeData.getRecentQnas());

        // productMap 생성 - order.html 방식으로 수정
        Map<String, ProductDTO> productMap = new HashMap<>();
        for (OrderItemDTO item : homeData.getRecentOrders()) {
            String prodNo = item.getProd_no();
            if (prodNo != null && !prodNo.isEmpty()) {
                try {
                    ProductDTO product = myService.getProduct3(prodNo);
                    if (product != null) {
                        log.debug("상품 로드 성공: prodNo={}, files={}",
                                prodNo,
                                product.getFiles() != null ? product.getFiles().size() : 0);
                        productMap.put(prodNo, product);
                    } else {
                        log.warn("상품 로드 실패 (null): prodNo={}", prodNo);
                    }
                } catch (Exception e) {
                    log.error("상품 로드 중 오류: prodNo={}", prodNo, e);
                }
            }
        }

        log.debug("productMap 최종 크기: {}", productMap.size());
        model.addAttribute("productMap", productMap);
    }
// MyController.java의 주문 섹션에 추가

    // ===================== 반품 신청 =====================
    @PostMapping("/order/return")
    @ResponseBody
    public ResponseEntity<String> returnOrder(@RequestParam("item_no") Long itemNo,
                                              @RequestParam("reason") String reason,
                                              @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memberId = user.getMember().getMem_id();

        try {
            myService.requestReturn(itemNo, memberId, reason);
            log.info("반품 신청 완료: item_no={}, mem_id={}", itemNo, memberId);
            return ResponseEntity.ok("success");
        } catch (IllegalArgumentException e) {
            log.error("반품 신청 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===================== 교환 신청 =====================
    @PostMapping("/order/exchange")
    @ResponseBody
    public ResponseEntity<String> exchangeOrder(@RequestParam("item_no") Long itemNo,
                                                @RequestParam("reason") String reason,
                                                @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memberId = user.getMember().getMem_id();

        try {
            myService.requestExchange(itemNo, memberId, reason);
            log.info("교환 신청 완료: item_no={}, mem_id={}", itemNo, memberId);
            return ResponseEntity.ok("success");
        } catch (IllegalArgumentException e) {
            log.error("교환 신청 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // ===================== 리뷰 =====================
    @GetMapping("/review")
    public String reviewPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal MyUserDetails user) {

        if (user == null) {
            return "redirect:/member/login";
        }

        String memberId = user.getMember().getMem_id();

        // 페이지네이션 설정 (10개씩)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("rev_rdate").descending());

        // 리뷰 페이지 조회
        Page<ReviewDTO> reviewPage = myService.getReviewsByMemIdPaged(memberId, pageable);
        model.addAttribute("reviewPage", reviewPage);

        // 마이페이지 요약 정보
        addMyPageSummary(model, memberId);

        return "my/review";
    }

    private List<String> saveFiles(List<MultipartFile> files) {
        List<String> savedFiles = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        File dest = new File(uploadDir + "review/" + filename);
                        dest.getParentFile().mkdirs();
                        file.transferTo(dest);
                        savedFiles.add(filename);
                        log.info("업로드 파일 저장 완료: {}", filename);
                    } catch (IOException e) {
                        log.error("파일 업로드 실패", e);
                    }
                }
            }
        }
        return savedFiles;
    }

    // ===================== 홈 =====================
    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return "redirect:/member/login";
        }

        addMyPageSummary(model, user.getMember().getMem_id());
        return "my/home";
    }

    // ===================== 회원 정보 =====================
    @GetMapping("/info")
    public String info(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        MemberDTO memberInfo = myService.getMemberInfo(memberId);
        model.addAttribute("memberInfo", memberInfo);
        addMyPageSummary(model, memberId);
        return "my/info";
    }

    @PostMapping("/info/update")
    @ResponseBody
    public ResponseEntity<String> updateInfo(@RequestBody MemberDTO memberDTO,
                                             @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        memberDTO.setMem_id(memberId);
        myService.updateMemberInfo(memberDTO);
        log.info("회원 정보 수정 성공: mem_id={}", memberId);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/info/changePassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordData,
                                                 @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            log.warn("비밀번호 변경 실패: 로그인 필요");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String newPassword = passwordData.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("새 비밀번호를 입력해주세요.");
        }

        String memberId = user.getMember().getMem_id();
        myService.changePassword(memberId, newPassword);
        log.info("비밀번호 변경 성공: mem_id={}", memberId);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/info/withdrawal")
    public String withdrawal(@AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            log.warn("회원 탈퇴 시도 실패: 로그인 필요");
            return "redirect:/member/login";
        }
        String memberId = user.getMember().getMem_id();
        myService.withdrawMember(memberId);
        log.info("회원 탈퇴 완료: mem_id={}", memberId);
        return "redirect:/member/login?withdrawal=true";
    }

    @GetMapping("/info/withdrawal")
    public String withdrawalPage() {
        return "my/withdrawal";
    }

    // ===================== 주문 =====================
    @PostMapping("/order/confirm")
    @ResponseBody
    public ResponseEntity<String> confirmOrder(@RequestParam("item_no") Long itemNo,
                                               @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memberId = user.getMember().getMem_id();
        myService.confirmOrder(itemNo, memberId);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/order/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@RequestParam("item_no") Long itemNo,
                                              @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memberId = user.getMember().getMem_id();
        myService.cancelOrder(itemNo, memberId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/order")
    public String orderPage(Model model, @AuthenticationPrincipal MyUserDetails user,
                            @PageableDefault(size = 5) Pageable pageable) { // size는 페이지네이션 5개씩 나오도록 설정
        String memberId = user.getMember().getMem_id();
        Page<OrderItemDTO> orderPage = myService.getOrdersByMemId(memberId, pageable);

        Map<String, ProductDTO> productMap = new HashMap<>();
        for (OrderItemDTO item : orderPage.getContent()) {
            ProductDTO product = myService.getProduct3(item.getProd_no());
            if (product != null) productMap.put(item.getProd_no(), product);
        }

        model.addAttribute("orderList", orderPage.getContent());
        model.addAttribute("productMap", productMap);
        model.addAttribute("currentPage", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        addMyPageSummary(model, memberId);
        return "my/order";
    }

    // ===================== 리뷰 작성 (Ajax + 파일 업로드) =====================
    @PostMapping("/review/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> writeReview(
            @RequestParam("prod_no") String prodNo,        // ✅ 추가
            @RequestParam("rating") int rating,
            @RequestParam("content") String content,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            @AuthenticationPrincipal MyUserDetails user
    ) {
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            log.warn("리뷰 작성 실패: 로그인 필요");
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        // ✅ prod_no 검증 추가
        if (prodNo == null || prodNo.trim().isEmpty()) {
            log.warn("리뷰 작성 실패: 상품번호 없음");
            result.put("success", false);
            result.put("message", "상품 정보를 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body(result);
        }

        List<MultipartFile> files = Arrays.asList(file1, file2, file3);
        List<String> savedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadDir + "review/" + filename);
                    dest.getParentFile().mkdirs();
                    file.transferTo(dest);
                    savedFiles.add(filename);
                    log.info("업로드 파일 저장 완료: {}", filename);
                } catch (IOException e) {
                    log.error("파일 업로드 실패", e);
                }
            }
        }

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setMem_id(user.getMember().getMem_id());
        reviewDTO.setProd_no(prodNo);  // ✅ prod_no 설정
        reviewDTO.setRev_rating(rating);
        reviewDTO.setRev_content(content);
        reviewDTO.setRev_files(savedFiles);

        ReviewDTO savedReview = myService.writeReview(reviewDTO);

        result.put("success", true);
        result.put("newReview", savedReview);
        result.put("savedFiles", savedFiles);

        log.info("리뷰 작성 성공: mem_id={}, prod_no={}, content={}",
                user.getMember().getMem_id(), prodNo, content);

        return ResponseEntity.ok(result);
    }
    // ===================== QnA =====================
    @PostMapping("/qna/write")
    @ResponseBody
    public ResponseEntity<String> writeQna(@ModelAttribute QnaDTO qnaDTO,
                                           @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memberId = user.getMember().getMem_id();
        qnaDTO.setMem_id(memberId);
        myService.writeQna(qnaDTO);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/qna")
    public String qnaPage(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        List<QnaDTO> recentQnas = myService.getRecentQnas(memberId);
        model.addAttribute("recentQnas", recentQnas);
        addMyPageSummary(model, memberId);
        return "my/qna";
    }

    // ===================== 쿠폰 =====================
    @GetMapping("/coupon")
    public String coupon(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        List<UserCouponDTO> userCoupons = myService.getUserCouponsByMemId(memberId);
        model.addAttribute("userCoupons", userCoupons);
        addMyPageSummary(model, memberId);
        return "my/coupon";
    }

    // ===================== 포인트 =====================
    @GetMapping("/point")
    public String point(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        List<PointDTO> pointHistory = myService.getPointHistory(memberId);
        model.addAttribute("pointHistory", pointHistory);
        addMyPageSummary(model, memberId);
        return "my/point";
    }

    // ===================== 상품 상세 =====================
    @GetMapping("/view/{prodNo}")
    public String viewProduct(@PathVariable String prodNo, Model model) {
        ProductDTO product = myService.getProduct3(prodNo);
        if (product != null) {
            if (product.getOptions() == null) product.setOptions(myService.getProductOption3(prodNo));
            if (product.getFiles() == null) product.setFiles(myService.getProductFiles(prodNo));
        }
        model.addAttribute("product", product);
        return "my/product/view";
    }
    @GetMapping("/order/detail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrderDetail(
            @RequestParam("ord_no") Long ordNo,
            @RequestParam("item_no") Long itemNo,
            @AuthenticationPrincipal MyUserDetails user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String memberId = user.getMember().getMem_id();
            Map<String, Object> orderDetail = myService.getOrderDetail(ordNo, itemNo, memberId);
            return ResponseEntity.ok(orderDetail);
        } catch (IllegalArgumentException e) {
            log.warn("주문상세 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            log.error("주문상세 조회 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}