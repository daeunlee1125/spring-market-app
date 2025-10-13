package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAddDTO {
    private String prod_no;
    private int cart_item_cnt;
    private String cart_option;
}