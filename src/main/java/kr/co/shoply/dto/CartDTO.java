package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private int cart_no;
    private String mem_id;
    private String prod_no;
    private int cart_item_cnt;
    private String cart_option;

    // 추가 필드
    private ProductDTO productDTO; // 장바구니 출력 용도
    private int totalPoint;     // 포인트 총합
    private String fName;          // 파일 경로
}
