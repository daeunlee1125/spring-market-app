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
        model.addAttribute("productDTO", productDTO);

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
    public String order(@RequestParam("cart_no") List<Integer> cart_no_list,
                        @AuthenticationPrincipal MyUserDetails myUserDetails,
                        Model model) {
        // 1. 로그인 여부 확인
        if (myUserDetails == null) {
            return "redirect:/member/login";
        }
        String username = myUserDetails.getUsername();
        MemberDTO memberDTO = memberService.getMember(username);                       // 주문자 정보 가져오기

        int totalprice = 0;
        int saleprice = 0;
        int totalPoint = productService.getPoint3(username);
        int totaldeliv = 0;
        List<CartDTO> cartDTOList = productService.getSelectedCartList3(cart_no_list); // 선택된 장바구니 리스트 가져오기
        for(CartDTO cartDTO : cartDTOList){                                            // 장바구니에 있는 상품 정보 가져오기
            cartDTO.setProductDTO(productService.getProduct3(cartDTO.getProd_no()));
            totalprice += cartDTO.getProductDTO().getProd_price() * cartDTO.getCart_item_cnt();
            saleprice += cartDTO.getProductDTO().getProd_price() - cartDTO.getProductDTO().getRealPrice();
            totaldeliv += cartDTO.getProductDTO().getProd_deliv_price();
        }
        List<SysCouponDTO> sysCouponDTOList = productService.getUserCoupon3(username); // 쿠폰 내역 확인

        model.addAttribute("totalPoint", totalPoint); // 주문자 보유 포인트
        model.addAttribute("saleprice", saleprice);   // 총 할인가격
        model.addAttribute("totalprice", totalprice); // 총 주문가격
        model.addAttribute("totaldeliv", totaldeliv); // 총 택배비
        model.addAttribute("cartDTOList", cartDTOList); // 상품 리스트
        model.addAttribute("memberDTO", memberDTO);   // 주문자 정보
        model.addAttribute("sysCouponDTOList", sysCouponDTOList); // 주문자 쿠폰 내역

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
                orderRequestDTO.getFinalAmount()
        );
        OrderDTO orderDTO = productService.getOrderNo(memId);

        // 4. insert orderItem table
        List<Integer> cartNoList = new ArrayList<>();
        for(int i = 0; i < orderRequestDTO.getOrderItems().size(); i++){
            cartNoList.add(orderRequestDTO.getOrderItems().get(i).getCart_id());
        }
        List<CartDTO> cartDTOList = productService.getSelectedCartList3(cartNoList);
        List<OrderItemDTO> orderItemDTOList =  new ArrayList<>();

        for(CartDTO cartDTO : cartDTOList){
            ProductDTO product = productService.getProduct3(cartDTO.getProd_no());
            cartDTO.setProductDTO(product);

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrd_no(orderDTO.getOrd_no());
            orderItemDTO.setProd_no(cartDTO.getProd_no());

            orderItemDTO.setItem_name(cartDTO.getProductDTO().getProd_name());
            orderItemDTO.setItem_cnt(cartDTO.getCart_item_cnt());
            orderItemDTOList.add(orderItemDTO);
        }
        productService.saveOrderItem3(orderItemDTOList);


        // --- JavaScript에 반환할 데이터 생성 ---
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderDTO.getMem_id()); // JS에서 페이지 이동 시 사용할 주문 번호
        response.put("cartNoList", cartNoList);
        if (cpCode != null && !cpCode.isEmpty()) {
            response.put("cpCode", cpCode);
        }
        response.put("usedPoint", orderRequestDTO.getUsedPoints());

        return response;
    }


    // ✅ 2. 주문 '완료 페이지'를 보여주는 View 메서드
    // 페이지 요청을 받아, 필요한 데이터를 조회하여 HTML 페이지를 렌더링합니다.
    @GetMapping("/product/complete") // GET 방식으로 변경
    public String showCompletePage(@RequestParam String orderId, @RequestParam List<Integer> cartNoList, @RequestParam String cpCode, @RequestParam int usedPoint, Model model) {

        // 서비스에 주문 정보와 주문 아이템 리스트를 함께 가져오는 메서드를 만듭니다.
        OrderDTO orderInfo = productService.getOrderById(orderId); // 주문 기본 정보 조회
        List<CompleteDTO> orderItems = productService.getCompleteOrder3(orderId, cartNoList); // 주문 상품 목록 조회
        log.info("orderInfo: " +  orderInfo.toString());
        log.info("orderItems: " +  orderItems.toString());

        int totalPrice = 0;
        int totalSalePrice = 0;
        int totalRealPrice = 0;
        int totalDeliv = 0;
        int totalPoint = 0;
        for(CompleteDTO completeDTO : orderItems){
            // 총 주문 금액
            totalPrice += completeDTO.getOrderItems().getProduct().getProd_price();

            // 상품별 실제 결제 금액
            completeDTO.getOrderItems().getProduct().setRealPrice(completeDTO.getOrderItems().getProduct().getProd_price() - completeDTO.getOrderItems().getProduct().getProd_price() * (completeDTO.getOrderItems().getProduct().getProd_sale() / 100));

            // 총 적립 포인트
            totalPoint += completeDTO.getOrderItems().getProduct().getProd_point();

            // 총 배송비
            totalDeliv += completeDTO.getOrderItems().getProduct().getProd_deliv_price();

            // 할인 금액
            completeDTO.getOrderItems().getProduct().setSaleprice(completeDTO.getOrderItems().getProduct().getProd_price() * (completeDTO.getOrderItems().getProduct().getProd_sale() / 100));

            // 총 할인 금액
            totalSalePrice += completeDTO.getOrderItems().getProduct().getSaleprice();
        }

        // 실제 결제 금액
        SysCouponDTO sysCouponDTO =  new SysCouponDTO();
        if (cpCode != null && !cpCode.isEmpty()) {
            sysCouponDTO = productService.getSysCoupon3(cpCode);
            model.addAttribute("sysCouponDTO", sysCouponDTO);
        }
        if (sysCouponDTO != null) {
            if (sysCouponDTO.getCp_type().equals(1)) {
                totalRealPrice = totalPrice - totalSalePrice - usedPoint - sysCouponDTO.getCp_value() + totalDeliv;
            }else if (sysCouponDTO.getCp_type().equals(2)) {
                totalRealPrice = totalPrice - totalSalePrice - usedPoint + totalDeliv;
                totalRealPrice *= (int) (sysCouponDTO.getCp_value() / 100.0);
            }else if (sysCouponDTO.getCp_type().equals(3)) {
                totalRealPrice = totalPrice - totalSalePrice - usedPoint;
            }else{
                totalRealPrice = totalPrice - totalSalePrice - usedPoint + totalDeliv;
            }
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
    public String search() {
        return "product/search";
    }



}
