package kr.co.shoply.dto;

import lombok.*;

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
}
