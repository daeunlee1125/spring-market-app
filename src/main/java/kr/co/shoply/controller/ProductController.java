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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

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

    @GetMapping("/product/complete")
    public String complete() {
        return "product/complete";
    }

    @GetMapping("/product/order")
    public String order() {
        return "product/order";
    }

    @GetMapping("/product/search")
    public String search() {
        return "product/search";
    }



}
