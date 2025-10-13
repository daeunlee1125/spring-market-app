package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartUpdateDTO {
    private int cart_no;
    private int cart_item_cnt;
}