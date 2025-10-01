package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdNoticeDTO {
    private int not_no;
    private String prod_no;
    private String not_name;
    private String not_val;
}
