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
    private MemberDTO memberDTO;
    private List<OrderItemData> orderItems; // CartItemDTO -> OrderItemData로 변경
    private String paymentMethod;
    private int finalAmount;
    private String productOption;

    @Data
    public static class OrderItemData { // 클래스명 및 필드 변경
        private Integer cart_id;     // 장바구니에서 온 경우에만 값이 있음 (Integer로 변경)
        private String prod_no;
        private int cart_item_cnt;
        private String prod_option;
    }
}