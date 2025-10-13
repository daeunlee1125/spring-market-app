// CartController.java
package kr.co.shoply.controller;

import kr.co.shoply.dto.CartAddDTO;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@Slf4j
@RestController // JSON 응답을 위한 컨트롤러
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService; // ProductService 주입

    @PostMapping("/cart/add")
    public ResponseEntity<?> addCartItem(@RequestBody List<CartAddDTO> cartItems,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) {

        log.info("장바구니 접근 성공 " + cartItems);
        if (myUserDetails == null) {
            // 비로그인 사용자는 로그인 페이지로 보내야 함 (프론트에서 처리해도 됨)
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            String mem_id = myUserDetails.getUsername();
            log.info("try문 접근 성공" + cartItems);
            for (CartAddDTO item : cartItems) {
                // 서비스의 insert 메서드 호출
                productService.insertCart3(
                        mem_id,
                        item.getProd_no(),
                        item.getCart_item_cnt(),
                        item.getCart_option()
                );
            }
            log.info("insert 성공" + cartItems);
            // 성공 시 success: true JSON 응답 반환
            return ResponseEntity.ok(Map.of("success", true));

        } catch (Exception e) {
            // 실패 시 에러 메시지와 함께 500 응답 반환
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}