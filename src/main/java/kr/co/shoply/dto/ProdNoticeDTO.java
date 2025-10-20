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
    private String pn_prod_no;
    private String tax_type;
    private String receipt_issue;
    private String business_type;
    private String origin;
    private String product_status;
}
