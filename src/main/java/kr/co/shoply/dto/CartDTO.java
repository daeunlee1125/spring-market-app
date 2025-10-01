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
}
