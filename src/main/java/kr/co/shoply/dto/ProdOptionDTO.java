package kr.co.shoply.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdOptionDTO {
    private int opt_no;
    private String prod_no;
    private String opt_name;
    private String opt_val;

    // 추가 필드
    private List<String> optValList; // opt_val에서 ,를 구분한 String list
}
