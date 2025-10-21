package kr.co.shoply.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Review;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.IndexService;
import kr.co.shoply.service.MyService;
import kr.co.shoply.service.ProductService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final SiteInfoService siteInfoService;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final IndexService indexService;

    // ===================== 공통 메소드 =====================
    private void addMyPageSummary(Model model, String memberId) {
        MyPageHomeDTO homeData = myService.getMyPageHomeData(memberId);

        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());
        model.addAttribute("recentOrders", homeData.getRecentOrders());
        model.addAttribute("recentPoints", homeData.getRecentPoints());
        model.addAttribute("recentReviews", homeData.getRecentReviews());
        model.addAttribute("recentQnas", homeData.getRecentQnas());

        // ✅ productMap 생성 - 이미지 경로 일관성
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


    // ===================== 문의하기 =====================
    @GetMapping("/qna")
    public String qnaPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal MyUserDetails user) {

        if (user == null) {
            return "redirect:/member/login";
        }

        String memberId = user.getMember().getMem_id();

        // ✅ 추가: 회원 정보 조회
        MemberDTO memberInfo = myService.getMemberInfo(memberId);
        model.addAttribute("memberInfo", memberInfo);

        Pageable pageable = PageRequest.of(page, 10, Sort.by("q_rdate").descending());

        Page<QnaDTO> qnaPage = myService.getQnasByMemIdPaged(memberId, pageable);
        model.addAttribute("qnaPage", qnaPage);

        addMyPageSummary(model, memberId);
        addBannerToModel(model);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();
        for (Cate1DTO cate1 : cate1DTOList) {
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());
            cate1.setSubCategories(subList);
        }
        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

        return "my/qna";
    }
    // ===================== 문의 작성 (Ajax) =====================
    @PostMapping("/qna/write")
    @ResponseBody
    public ResponseEntity<String> writeQna(
            @RequestParam("qna_type") String qnaType,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @AuthenticationPrincipal MyUserDetails user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            String memberId = user.getMember().getMem_id();

            // 입력값 검증
            if (qnaType == null || qnaType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("문의유형을 선택해주세요.");
            }
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("제목을 입력해주세요.");
            }
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("내용을 입력해주세요.");
            }

            QnaDTO qnaDTO = new QnaDTO();
            qnaDTO.setMem_id(memberId);
            qnaDTO.setQ_cate1(qnaType);  // 상품/배송/기타
            qnaDTO.setQ_title(title);
            qnaDTO.setQ_content(content);

            myService.writeQna(qnaDTO);

            log.info("문의 작성 완료: mem_id={}, title={}", memberId, title);
            return ResponseEntity.ok("success");

        } catch (Exception e) {
            log.error("문의 작성 중 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문의 작성 중 오류가 발생했습니다.");
        }
    }
    // ===================== 홈 =====================
    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal MyUserDetails user) {
        if (user == null) {
            return "redirect:/member/login";
        }

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();
        for (Cate1DTO cate1 : cate1DTOList) {
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());
            cate1.setSubCategories(subList);
        }
        model.addAttribute("cate1DTOList", cate1DTOList);

        addMyPageSummary(model, user.getMember().getMem_id());
        addBannerToModel(model);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

        // ✅ 추가: 각 상품별 리뷰 작성 여부 확인
        MyPageHomeDTO homeData = myService.getMyPageHomeData(memId);
        Map<String, Boolean> reviewCheckMap = new HashMap<>();

        for (OrderItemDTO item : homeData.getRecentOrders()) {
            String prodNo = item.getProd_no();
            if (prodNo != null && !prodNo.isEmpty()) {
                Review review = myService.findReviewByMemIdAndProdNo(memId, prodNo);
                reviewCheckMap.put(prodNo, review != null);
            }
        }
        model.addAttribute("reviewCheckMap", reviewCheckMap);

        return "my/home";
    }


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

        Pageable pageable = PageRequest.of(page, 10, Sort.by("rev_rdate").descending());

        Page<ReviewDTO> reviewPage = myService.getReviewsByMemIdPaged(memberId, pageable);
        model.addAttribute("reviewPage", reviewPage);

        addMyPageSummary(model, memberId);
        addBannerToModel(model);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

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

    // ===================== 회원 정보 =====================
    @GetMapping("/info")
    public String info(Model model, @AuthenticationPrincipal MyUserDetails user) {
        String memberId = user.getMember().getMem_id();
        MemberDTO memberInfo = myService.getMemberInfo(memberId);
        model.addAttribute("memberInfo", memberInfo);

        BannerDTO banner = myService.getBannerByNo(1);
        model.addAttribute("banner", banner);
        addBannerToModel(model);
        addMyPageSummary(model, memberId);
        addBannerToModel(model);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

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

        String oldPassword = passwordData.get("currentPassword");

        if (passwordEncoder.matches(oldPassword, user.getPassword())){
            String memberId = user.getMember().getMem_id();
            myService.changePassword(memberId, newPassword);
            log.info("비밀번호 변경 성공: mem_id={}", memberId);
            return ResponseEntity.ok("success");
        }else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("현재 비밀번호가 일치하지 않습니다.");
        }

    }

    @PostMapping("/info/withdrawal")
    public String withdrawal(@AuthenticationPrincipal MyUserDetails user, HttpServletRequest request) {
        if (user == null) {
            log.warn("회원 탈퇴 시도 실패: 로그인 필요");
            return "redirect:/member/login";
        }
        String memberId = user.getMember().getMem_id();
        myService.withdrawMember(memberId);
        request.getSession().invalidate();
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



    @GetMapping("/review/detail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getReviewDetail(
            @RequestParam("prod_no") String prodNo,
            @AuthenticationPrincipal MyUserDetails user) {

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            log.warn("리뷰 조회 실패: 로그인 필요");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        try {
            String memberId = user.getMember().getMem_id();

            if (prodNo == null || prodNo.trim().isEmpty()) {
                log.warn("리뷰 조회 실패: prodNo가 null 또는 empty");
                result.put("error", "상품번호가 없습니다.");
                return ResponseEntity.badRequest().body(result);
            }

            Review review = myService.findReviewByMemIdAndProdNo(memberId, prodNo);

            if (review == null) {
                log.warn("리뷰 조회 실패: 리뷰를 찾을 수 없음 - mem_id={}, prod_no={}", memberId, prodNo);
                result.put("error", "리뷰를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }

            Integer rating = review.getRev_rating();
            String content = review.getRev_content();
            Date rdate = review.getRev_rdate();
            String imgPath = review.getRev_img_path();

            log.debug("리뷰 조회 성공 - rating={}, content_length={}, imgPath={}",
                    rating, content != null ? content.length() : 0, imgPath);

            result.put("rev_rating", rating != null ? rating : 0);
            result.put("rev_content", content != null ? content : "");

            // 날짜 포맷팅
            if (rdate != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                result.put("rev_rdate", sdf.format(rdate));
            } else {
                result.put("rev_rdate", "");
            }

            // ✅ 수정: 경로 변환 로직 개선
            List<String> files = new ArrayList<>();
            if (imgPath != null && !imgPath.isEmpty()) {
                String[] fileArray = imgPath.contains(",")
                        ? imgPath.split(",\\s*")
                        : new String[]{imgPath};

                for (String file : fileArray) {
                    if (file != null && !file.isEmpty()) {
                        String processedPath = file.trim();

                        // ✅ 파일명만 있으면 /shoply/uploads/review/ 경로 추가
                        if (!processedPath.startsWith("/")) {
                            processedPath = "/shoply/uploads/review/" + processedPath;
                        } else if (processedPath.startsWith("/uploads/")) {
                            // /uploads/review/xxx → /shoply/uploads/review/xxx
                            processedPath = "/shoply" + processedPath;
                        } else if (!processedPath.startsWith("/shoply/")) {
                            // 다른 경로면 /shoply 추가
                            processedPath = "/shoply" + processedPath;
                        }

                        files.add(processedPath);
                        log.debug("변환된 리뷰 이미지 경로: {} → {}", file, processedPath);
                    }
                }
            }
            result.put("rev_files", files);

            log.info("리뷰 조회 완료 - mem_id={}, prod_no={}, files_count={}",
                    memberId, prodNo, files.size());

            return ResponseEntity.ok(result);

        } catch (NullPointerException e) {
            log.error("리뷰 조회 중 NullPointerException 발생", e);
            result.put("error", "데이터 처리 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } catch (Exception e) {
            log.error("리뷰 조회 중 예외 발생", e);
            result.put("error", "서버 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    @GetMapping("/order")
    public String orderPage(Model model, @AuthenticationPrincipal MyUserDetails user,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "periodType", required = false) String periodType,
                            @RequestParam(value = "period", required = false) String period,
                            @RequestParam(value = "startMonth", required = false) String startMonth,
                            @RequestParam(value = "endMonth", required = false) String endMonth) {

        String memberId = user.getMember().getMem_id();

        // ✅ 명시적으로 Pageable 생성
        Pageable pageable = PageRequest.of(page, 10, Sort.by("ord_date").descending());

        Page<OrderItemDTO> orderPage = myService.getOrdersByMemIdWithPeriod(
                memberId, pageable, periodType, period, startMonth, endMonth);

        Map<String, ProductDTO> productMap = new HashMap<>();
        for (OrderItemDTO item : orderPage.getContent()) {
            ProductDTO product = myService.getProduct3(item.getProd_no());
            if (product != null) productMap.put(item.getProd_no(), product);
        }

        // ✅ 리뷰 체크맵 생성
        Map<String, Boolean> reviewCheckMap = new HashMap<>();
        for (OrderItemDTO item : orderPage.getContent()) {
            String prodNo = item.getProd_no();
            if (prodNo != null && !prodNo.isEmpty()) {
                Review review = myService.findReviewByMemIdAndProdNo(memberId, prodNo);
                reviewCheckMap.put(prodNo, review != null);
            }
        }
        model.addAttribute("reviewCheckMap", reviewCheckMap);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("productMap", productMap);

        // ✅ 필터 파라미터 전달 (페이지네이션에서 사용)
        model.addAttribute("periodType", periodType);
        model.addAttribute("period", period);
        model.addAttribute("startMonth", startMonth);
        model.addAttribute("endMonth", endMonth);

        addMyPageSummary(model, memberId);
        addBannerToModel(model);
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();
        for (Cate1DTO cate1 : cate1DTOList) {
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());
            cate1.setSubCategories(subList);
        }
        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

        return "my/order";
    }

    // ===================== 리뷰 작성 (Ajax + 파일 업로드) =====================
    @PostMapping("/review/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> writeReview(
            @RequestParam("prod_no") String prodNo,
            @RequestParam("rating") int rating,
            @RequestParam("content") String content,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            @AuthenticationPrincipal MyUserDetails user
    ) {
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        String uploadDir = "/home/ec2-user/shoply/uploads/review/";
        List<MultipartFile> files = Arrays.asList(file1, file2, file3);
        List<String> savedFiles = new ArrayList<>();

        try {
            // 디렉토리 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                log.info("디렉토리 생성 결과: {}, 경로: {}", created, uploadDir);
            }

            log.info("✅ 업로드 디렉토리 확인: {}", uploadDirFile.exists());
            log.info("✅ 쓰기 권한: {}", uploadDirFile.canWrite());

            // 파일 저장
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    String filePath = uploadDir + filename;

                    log.info("파일 저장 시작: {}", filePath);

                    try {
                        File dest = new File(filePath);
                        file.transferTo(dest);

                        savedFiles.add(filename);
                        log.info("✅ 파일 저장 성공: {}", filename);
                        log.info("파일 존재 확인: {}", dest.exists());
                    } catch (IOException e) {
                        log.error("❌ 파일 저장 실패: {}", filename, e);
                    }
                }
            }

            log.info("✅ 최종 저장된 파일: {}", savedFiles);

            // DB에 저장
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setMem_id(user.getMember().getMem_id());
            reviewDTO.setProd_no(prodNo);
            reviewDTO.setRev_rating(rating);
            reviewDTO.setRev_content(content);
            reviewDTO.setRev_files(savedFiles);

            ReviewDTO savedReview = myService.writeReview(reviewDTO);

            result.put("success", true);
            result.put("newReview", savedReview);
            result.put("savedFiles", savedFiles);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("❌ 리뷰 작성 중 오류 발생", e);
            result.put("success", false);
            result.put("message", "리뷰 작성 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===================== 쿠폰 =====================
    @GetMapping("/coupon")
    public String coupon(Model model,
                         @AuthenticationPrincipal MyUserDetails user,
                         @PageableDefault(size = 10) Pageable pageable) {
        String memberId = user.getMember().getMem_id();

        List<UserCouponDTO> userCoupons = myService.getUserCouponsByMemId(memberId);
        model.addAttribute("userCoupons", userCoupons);

        Page<UserCouponDTO> couponPage = myService.getUserCouponsByMemIdPaged(memberId, pageable);
        model.addAttribute("couponPage", couponPage);

        addMyPageSummary(model, memberId);
        addBannerToModel(model);
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

        return "my/coupon";
    }

    // ===================== 포인트 =====================
    @GetMapping("/point")
    public String point(Model model,
                        @AuthenticationPrincipal MyUserDetails user,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "periodType", required = false) String periodType,
                        @RequestParam(value = "period", required = false) String period,
                        @RequestParam(value = "startMonth", required = false) String startMonth,
                        @RequestParam(value = "endMonth", required = false) String endMonth) {

        String memberId = user.getMember().getMem_id();

        // ✅ 명시적으로 Pageable 생성
        Pageable pageable = PageRequest.of(page, 10, Sort.by("p_date").descending());

        myService.debugPointData(memberId);

        Page<PointDTO> pointPage = myService.getPointHistoryPagedWithPeriod(
                memberId, pageable, periodType, period, startMonth, endMonth);

        model.addAttribute("pointPage", pointPage);
        model.addAttribute("periodType", periodType);
        model.addAttribute("period", period);
        model.addAttribute("startMonth", startMonth);
        model.addAttribute("endMonth", endMonth);

        addBannerToModel(model);
        addMyPageSummary(model, memberId);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();
        for (Cate1DTO cate1 : cate1DTOList) {
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());
            cate1.setSubCategories(subList);
        }
        model.addAttribute("cate1DTOList", cate1DTOList);

        String memId = user.getUsername();
        int cartCount = indexService.getCartCount3(memId);
        model.addAttribute("cartCount", cartCount);

        return "my/point";
    }

    // ===================== 상품 상세 =====================
    @GetMapping("/view/{prodNo}")
    public String viewProduct(@PathVariable String prodNo, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        ProductDTO product = myService.getProduct3(prodNo);
        if (product != null) {
            if (product.getOptions() == null) product.setOptions(myService.getProductOption3(prodNo));
            if (product.getFiles() == null) product.setFiles(myService.getProductFiles(prodNo));
        }
        model.addAttribute("product", product);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "my/product/view";
    }


    // 마이페이지 배너 조회 메서드 (공통)
    private void addBannerToModel(Model model) {
        // ban_location = 5: 마이페이지 배너
        List<BannerDTO> myPageBanners = myService.getBannerByLocation(5);
        if (myPageBanners != null && !myPageBanners.isEmpty()) {
            model.addAttribute("banner", myPageBanners.get(0)); // 첫 번째 배너 사용
        }
    }

    @GetMapping("/seller/info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSellerInfo(@RequestParam("mem_id") String memId) {
        try {
            Map<String, Object> sellerInfo = myService.getSellerInfo(memId);
            log.info("판매자 정보 API 호출 성공: mem_id={}", memId);
            return ResponseEntity.ok(sellerInfo);
        } catch (IllegalArgumentException e) {
            log.warn("판매자 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("판매자 정보 조회 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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