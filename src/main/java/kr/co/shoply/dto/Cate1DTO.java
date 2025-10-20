package kr.co.shoply.dto;

import lombok.*;

import java.util.List;

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
    private List<Cate2DTO> subCategories;

}
