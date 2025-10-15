package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Cate1DTO {

    private int cate1_no;
    private String cate1_name;

    // 추가 필드
    private int cate1_sell;
}
