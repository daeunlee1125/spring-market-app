package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.Cate2Service;
import kr.co.shoply.service.MemberService;
import kr.co.shoply.service.ProductService;
import kr.co.shoply.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final Cate2Service cate2Service;
    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping("/product/list/{cate2No}")
    public String list(@PathVariable int cate2No, Model model) {
        return "redirect:/product/list/" + cate2No + "/sold";
    }

    @GetMapping("/product/list/{cate2No}/{sort}")
    public String sortList(@PathVariable int cate2No, @PathVariable String sort, Model model) {
        List<ProductDTO> productDTOList = productService.getProductAll3(cate2No, sort);

        Cate2DTO cate2DTO = cate2Service.getCate(cate2No);

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("cate2DTO", cate2DTO);
        model.addAttribute("sort", sort);
        return "product/list";
    }

    @GetMapping("/product/view/{cate2No}/{prodNo}")
    public String view(@PathVariable int cate2No, @PathVariable String prodNo, Model model) {
        Cate2DTO cate2DTO = cate2Service.getCate(cate2No);
        model.addAttribute("cate2DTO", cate2DTO);

        ProductDTO productDTO = productService.getProduct3(prodNo);
        log.info("productDTO={}", productDTO);
        if (productDTO == null) {
            log.warn("존재하지 않는 상품 번호로 접근: {}", prodNo);
            // 존재하지 않는 상품이므로 상품 목록 페이지로 리다이렉트
            return "redirect:/product/list/" + cate2No;
        }
        model.addAttribute("productDTO", productDTO);

        List<ProFileDTO> proFileDTOList = productService.getFiles3(prodNo);
        // 2. f_dist를 Key로 사용하는 Map 생성
        Map<Integer, String> prodPathMap = new HashMap<>();

        // 3. 리스트를 반복하며 f_dist를 Key, f_name을 Value로 Map에 담기
        for (ProFileDTO proFileDTO : proFileDTOList) {
            prodPathMap.put(proFileDTO.getF_dist(), proFileDTO.getF_name());
        }

        // 4. 완성된 Map을 Model에 추가
        model.addAttribute("prodPathMap", prodPathMap);


        MemberDTO memberDTO = memberService.getMemberAddr(productDTO.getMem_id(), productDTO.getProd_no());
        model.addAttribute("memberDTO", memberDTO);

        LocalDate localDate = LocalDate.now(); // 배송예정일
        localDate = localDate.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        String formattedDate = localDate.format(formatter);
        model.addAttribute("formattedDate", formattedDate);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek(); // 배송예정 요일
        String week = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
        week = week.substring(0,1);
        model.addAttribute("week", week);

        // ✅ 1. 리뷰 총 개수 가져오기 (이전 코드 리뷰에서 수정한 getCountReviews 사용)
        int totalReviewCount = reviewService.getCountReviews(prodNo);

        // ✅ 2. 총 페이지 수 계산 ( (총 개수 + 페이지당 개수 - 1) / 페이지당 개수 )
        int totalPages = (totalReviewCount + 4) / 5;

        // ✅ 3. 계산된 총 페이지 수를 모델에 추가
        model.addAttribute("totalPages", totalPages);

        // 첫 페이지 리뷰 목록 가져오기
        List<ReviewDTO> reviewDTOList = reviewService.getPageList(prodNo, 1);
        model.addAttribute("reviewDTOList", reviewDTOList);

        List<ProdOptionDTO> OpDtoList = productService.getProductOption3(prodNo); // 상품별 옵션들
        for(ProdOptionDTO opDto : OpDtoList){
            // 콤마(,)로 구분된 문자열을 List<String>으로 변환
            List<String> values = Arrays.asList(opDto.getOpt_val().split("\\s*,\\s*"));
            opDto.setOptValList(values);
        }
        model.addAttribute("OpDtoList", OpDtoList);

        return "product/view";
    }

    @GetMapping("/product/cart")
    public String cart(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        if (myUserDetails == null) { // 로그인 확인
            return "redirect:/member/login";
        }
        String userName = myUserDetails.getUsername(); // 현재 로그인 된 id 가져오기
        List<CartDTO> cartDTOList = productService.getCartAll3(userName);
        model.addAttribute("cartDTOList", cartDTOList);

        return "product/cart";
    }

    @PostMapping("/product/delete")
    public String deleteCart(@RequestParam("cart_no") int cart_no) {
        productService.deleteCart3(cart_no);

        return  "redirect:/product/cart";
    }

    @DeleteMapping("/product/delete/selected")
    public ResponseEntity<?> deleteCarts(@RequestBody List<Integer> cart_no_list) {
        try {
            // 서비스 메서드를 호출하여 리스트에 담긴 모든 cart_no를 삭제합니다.
            // 서비스와 매퍼(MyBatis)에서는 이 리스트를 받아 반복 처리해야 합니다.
            productService.deleteSelectedCarts3(cart_no_list);
            return ResponseEntity.ok().build(); // 성공 시 200 OK 응답 반환
        } catch (Exception e) {
            log.error("선택 삭제 에러", e);
            return ResponseEntity.internalServerError().build(); // 실패 시 500 에러 응답 반환
        }
    }

    @PatchMapping("/product/cart/update")
    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody CartUpdateDTO cartUpdateDTO) {
        try {
            productService.updateCartQuantity(cartUpdateDTO);
            return ResponseEntity.ok().build(); // 성공 시 200 OK 응답
        } catch (Exception e) {
            // 로그 기록 등 예외 처리
            return ResponseEntity.internalServerError().build(); // 실패 시 500 에러 응답
        }
    }

    @PostMapping("/product/order")
    public String order(
                        // 장바구니에서 올 때는 이 파라미터에 값이 담김 (필수가 아님)
                        @RequestParam(value = "cart_no", required = false) List<Integer> cart_no_list,

                        // 바로구매로 올 때는 이 파라미터에 값이 담김
                        @ModelAttribute DirectOrderFormDTO directOrder,

                        @AuthenticationPrincipal MyUserDetails myUserDetails,
                        Model model) {

        if (myUserDetails == null) {
            return "redirect:/member/login";
        }
        String username = myUserDetails.getUsername();

        // 주문 페이지에 전달할 상품 목록 (어떤 경로로 오든 이 리스트를 채우는 것이 목표)
        List<CartDTO> orderProductList;

        // --- 분기 처리 ---
        if (cart_no_list != null && !cart_no_list.isEmpty()) {
            // CASE 1: 장바구니를 통해 주문 (기존 로직)
            orderProductList = productService.getSelectedCartList3(cart_no_list);

        } else if (directOrder.getOrderItems() != null && !directOrder.getOrderItems().isEmpty()) {
            // CASE 2: 상품 상세페이지에서 바로구매
            orderProductList = new ArrayList<>();
            for (DirectOrderItemDTO item : directOrder.getOrderItems()) {
                // Form으로 받은 데이터를 CartDTO 형식으로 변환
                CartDTO cartDTO = new CartDTO();
                cartDTO.setProd_no(item.getProd_no());
                cartDTO.setCart_item_cnt(item.getCart_item_cnt());
                cartDTO.setCart_option(item.getCart_option());
                orderProductList.add(cartDTO);
            }
        } else {
            // 처리할 상품이 없는 예외적인 경우
            // 예를 들어 장바구니로 리다이렉트
            return "redirect:/product/cart";
        }

        // --- 이하 공통 로직 ---
        // (어떤 경로로 왔든 'orderProductList'가 채워져 있으므로 동일하게 사용 가능)
        MemberDTO memberDTO = memberService.getMember(username);
        int totalprice = 0;
        int saleprice = 0;
        int totalPoint = productService.getPoint3(username);
        int totaldeliv = 0;

        for (CartDTO cartDTO : orderProductList) {
            ProductDTO productDTO = productService.getProduct3(cartDTO.getProd_no());
            cartDTO.setProductDTO(productDTO);
            totalprice += productDTO.getProd_price() * cartDTO.getCart_item_cnt();
            saleprice += (productDTO.getProd_price() - productDTO.getRealPrice()) * cartDTO.getCart_item_cnt();
            totaldeliv += productDTO.getProd_deliv_price();
        }
        List<SysCouponDTO> sysCouponDTOList = productService.getUserCoupon3(username);

        model.addAttribute("totalPoint", totalPoint);
        model.addAttribute("saleprice", saleprice);
        model.addAttribute("totalprice", totalprice);
        model.addAttribute("totaldeliv", totaldeliv);
        model.addAttribute("cartDTOList", orderProductList); // 뷰에는 항상 cartDTOList 이름으로 전달
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("sysCouponDTOList", sysCouponDTOList);

        return "product/order";
    }

    @PostMapping("/api/product/order")
    @ResponseBody // JSON 응답을 위해 필수!
    public Map<String, Object> processOrderAPI(@RequestBody OrderRequestDTO orderRequestDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) {

        String memId = myUserDetails.getUsername();

        // --- 기존 주문 처리 로직 수행 ---
        // 1. 사용한 쿠폰 상태 업데이트
        String cpCode = orderRequestDTO.getUsedCouponId();
        if (cpCode != null && !cpCode.isEmpty()) {
            productService.modifyUsedCoupon3(cpCode, memId);
        }

        // 2. 사용한 포인트 삭감
        if(orderRequestDTO.getUsedPoints() > 0) {
            int usedPoint = -orderRequestDTO.getUsedPoints(); // 음수로 변환
            productService.saveUsedCoupon3(memId, 2, usedPoint, "포인트 사용");
        }

        // 3. insert order table.
        productService.saveOrder3(
                memId,
                orderRequestDTO.getMemberDTO().getMem_name(),
                orderRequestDTO.getMemberDTO().getMem_hp(),
                orderRequestDTO.getMemberDTO().getMem_zip(),
                orderRequestDTO.getMemberDTO().getMem_addr1(),
                orderRequestDTO.getMemberDTO().getMem_addr2(),
                orderRequestDTO.getPaymentMethod(),
                orderRequestDTO.getFinalAmount()
        );
        OrderDTO orderDTO = productService.getOrderNo(memId);

        // 4. insert orderItem table
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        List<Integer> cartNoListToDelete = new ArrayList<>(); // 삭제할 장바구니 번호 리스트

        // DTO로 직접 받은 상품 정보로 OrderItemDTO를 만듦
        for (OrderRequestDTO.OrderItemData itemData : orderRequestDTO.getOrderItems()) {
            ProductDTO product = productService.getProduct3(itemData.getProd_no());

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrd_no(orderDTO.getOrd_no());
            orderItemDTO.setProd_no(itemData.getProd_no());
            orderItemDTO.setItem_name(product.getProd_name()); // 상품명은 DB에서 다시 조회
            orderItemDTO.setItem_cnt(itemData.getCart_item_cnt());
            orderItemDTO.setProd_option(itemData.getProd_option());
            orderItemDTOList.add(orderItemDTO);

            // 만약 cart_id가 있다면 (장바구니에서 온 상품이라면) 삭제 목록에 추가
            if (itemData.getCart_id() != null) {
                cartNoListToDelete.add(itemData.getCart_id());
            }
        }

        // 이 부분은 비어있지 않으므로 에러가 발생하지 않음
        productService.saveOrderItem3(orderItemDTOList);

        // 5. cart delete (삭제할 항목이 있을 때만 실행)
        if (!cartNoListToDelete.isEmpty()) {
            productService.deleteSelectedCarts3(cartNoListToDelete);
        }


        // --- JavaScript에 반환할 데이터 생성 ---
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderDTO.getOrd_no()); // JS에서 페이지 이동 시 사용할 주문 번호
        response.put("cartNoList", cartNoListToDelete);
        if (cpCode != null && !cpCode.isEmpty()) {
            response.put("cpCode", cpCode);
        }
        response.put("usedPoint", orderRequestDTO.getUsedPoints());

        return response;
    }


    // ✅ 2. 주문 '완료 페이지'를 보여주는 View 메서드
    // 페이지 요청을 받아, 필요한 데이터를 조회하여 HTML 페이지를 렌더링합니다.
    @GetMapping("/product/complete") // GET 방식으로 변경
    public String showCompletePage(@RequestParam String orderId,
                                   @RequestParam(required = false) String cpCode,
                                   @RequestParam int usedPoint,
                                   Model model) {

        // 서비스에 주문 정보와 주문 아이템 리스트를 함께 가져오는 메서드를 만듭니다.
        OrderDTO orderInfo = productService.getOrderById(orderId); // 주문 기본 정보 조회
        List<CompleteDTO> orderItems = productService.getCompleteOrder3(orderId); // 주문 상품 목록 조회

        // 날짜 String으로 설정
        // 1. 원본 문자열의 형식에 맞는 포매터를 준비해 LocalDateTime 객체로 변환
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(orderInfo.getOrd_date(), inputFormatter);

        // 2. 원하는 출력 형식("yyyy년 MM월 dd일")의 포매터를 새로 정의
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        // 3. LocalDateTime 객체를 새로운 포매터로 포맷팅하여 문자열로 변환
        String formattedDate = dateTime.format(outputFormatter);

        // 4. 다시 저장
        orderInfo.setOrd_date(formattedDate);

        int totalPrice = 0;
        int totalSalePrice = 0;
        int totalRealPrice = 0;
        int totalDeliv = 0;
        int totalPoint = 0;
        for(CompleteDTO completeDTO : orderItems){
            // 총 주문 금액
            totalPrice += completeDTO.getOrderItems().getProduct().getProd_price() * completeDTO.getOrderItems().getItem_cnt();

            // 상품별 실제 결제 금액
            completeDTO.getOrderItems().getProduct().setRealPrice(
                    completeDTO.getOrderItems().getProduct().getProd_price()
                    - (int) (
                    completeDTO.getOrderItems().getProduct().getProd_price()
                            *
                            (completeDTO.getOrderItems().getProduct().getProd_sale() / 100.0)
                    ) * completeDTO.getOrderItems().getItem_cnt()
            );

            // 총 적립 포인트
            totalPoint += completeDTO.getOrderItems().getProduct().getProd_point() * completeDTO.getOrderItems().getItem_cnt();

            // 총 배송비
            totalDeliv += completeDTO.getOrderItems().getProduct().getProd_deliv_price();

            // 할인 금액
            completeDTO.getOrderItems().getProduct().setSaleprice((int) (completeDTO.getOrderItems().getProduct().getProd_price()
                        *
                        (completeDTO.getOrderItems().getProduct().getProd_sale() / 100.0))
                    *
                    completeDTO.getOrderItems().getItem_cnt()
            );

            // 총 할인 금액
            totalSalePrice += completeDTO.getOrderItems().getProduct().getSaleprice();
        }

        // 실제 결제 금액
        // 1. sysCouponDTO를 null로 초기화
        SysCouponDTO sysCouponDTO = null;

        // 2. cpCode가 있을 때만 DB에서 쿠폰 정보를 조회
        if (cpCode != null && !cpCode.isEmpty()) {
            sysCouponDTO = productService.getSysCoupon3(cpCode);
        }

        // 4. sysCouponDTO 객체와 그 내부의 cp_type이 모두 null이 아닐 때만 쿠폰 할인 로직 실행
        if (sysCouponDTO != null && sysCouponDTO.getCp_type() != null) {

            model.addAttribute("sysCouponDTO", sysCouponDTO); // 뷰에 쿠폰 정보 전달

            Integer cpType = sysCouponDTO.getCp_type(); // Integer 타입으로 받음

            if (cpType.equals(1)) { // 쿠폰 타입 1 (정액 할인)
                totalRealPrice = totalPrice - totalSalePrice - usedPoint - sysCouponDTO.getCp_value() + totalDeliv;
            } else if (cpType.equals(2)) { // 쿠폰 타입 2 (정률 할인)
                // 정률 할인은 배송비 제외 금액에 적용하는 것이 일반적
                int discountedPrice = totalPrice - totalSalePrice - usedPoint;
                int couponDiscountAmount = (int) (discountedPrice * (sysCouponDTO.getCp_value() / 100.0));
                totalRealPrice = discountedPrice - couponDiscountAmount + totalDeliv;
            } else if (cpType.equals(3)) { // 쿠폰 타입 3 (배송비 무료)
                totalRealPrice = totalPrice - totalSalePrice - usedPoint;
                // totalDeliv를 0으로 만드는 대신, 최종 금액 계산에서 제외
            } else { // 그 외 타입 (쿠폰 적용 안 함)
                totalRealPrice = totalPrice - totalSalePrice - usedPoint + totalDeliv;
            }
        } else {
            // 쿠폰을 사용하지 않았거나, 유효하지 않은 쿠폰일 경우
            totalRealPrice = totalPrice - totalSalePrice - usedPoint + totalDeliv;
        }


        // 조회한 정보를 Model에 담아 View로 전달
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalSalePrice", totalSalePrice);
        model.addAttribute("totalRealPrice", totalRealPrice);
        model.addAttribute("totalDeliv", totalDeliv);
        model.addAttribute("totalPoint", totalPoint);

        return "product/complete";
    }

    @GetMapping("/product/search")
    public String unifiedSearch(
            @RequestParam("keyword") String keyword,
            @RequestParam(required = false) String keyword2,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "sold") String sort,
            Model model) {

        List<ProductDTO> productDTOList;

        // --- 💡 분기 처리 로직 💡 ---
        // 2차 검색 조건(keyword2, type 등)이 하나라도 있는지 확인
        if ((keyword2 != null && !keyword2.isEmpty()) ||
                (type != null && !type.isEmpty()) ||
                (minPrice != null) || (maxPrice != null)) {

            // CASE 2: 2차 검색 수행
            // DTO를 생성하지 않고 파라미터를 서비스 메서드에 직접 전달합니다.
            productDTOList = productService.getSearch2Product3(keyword, sort, type, keyword2, minPrice, maxPrice);

        } else {
            // CASE 1: 1차 검색만 수행
            productDTOList = productService.getSearchProduct3(keyword, sort);
        }

        // --- 이하 공통 로직 ---
        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);

        // DTO 대신, 2차 검색 조건들을 개별적으로 모델에 담아 뷰에서 사용합니다.
        model.addAttribute("keyword2", keyword2);
        model.addAttribute("type", type);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product/search";
    }

}
