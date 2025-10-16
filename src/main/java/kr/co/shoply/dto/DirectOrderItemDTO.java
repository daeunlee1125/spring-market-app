package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 바로구매 시 개별 상품의 정보를 담는 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectOrderItemDTO {
    private String prod_no;
    private int cart_item_cnt;
    private String cart_option;
}