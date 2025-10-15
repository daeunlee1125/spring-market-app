package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {
    private int usedPoints;
    private String usedCouponId;
    private MemberDTO memberDTO; // 기존 MemberDTO 재사용 또는 새로 정의
    private List<CartItemDTO> orderItems;
    private String paymentMethod; // 결제방법
    private int finalAmount;
    private String productOption;

    @Data
    public static class CartItemDTO {
        private int cart_id;
    }
}